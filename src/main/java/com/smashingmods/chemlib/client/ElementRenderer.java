package com.smashingmods.chemlib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.smashingmods.chemlib.common.items.ElementItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/** Retains the element model, display transforms and GUI/frame abbreviation labels. */
public record ElementRenderer(ItemModel base) implements ItemModel {
    private static final Renderer RENDERER = new Renderer();

    @Override
    public void update(ItemStackRenderState output, ItemStack stack, ItemModelResolver resolver,
                       ItemDisplayContext mode, ClientLevel level, ItemOwner owner, int seed) {
        ItemStackRenderState model = new ItemStackRenderState();
        base.update(model, stack, resolver, mode, level, owner, seed);
        String abbreviation = ((ElementItem) stack.getItem()).getAbbreviation();
        RenderData data = new RenderData(model, mode, abbreviation);
        ItemStackRenderState.LayerRenderState layer = output.newLayer();
        layer.setupSpecialModel(RENDERER, data);
        layer.setUsesBlockLight(false);
        List<Vector3fc> extents = new ArrayList<>();
        PoseStack pose = new PoseStack();
        transformModel(pose, mode);
        model.visitExtents(v -> extents.add(new Vector3f(v).mulPosition(pose.last().pose())));
        layer.setExtents(() -> extents.toArray(Vector3fc[]::new));
        output.appendModelIdentityElement(this);
        output.appendModelIdentityElement(mode);
        output.appendModelIdentityElement(abbreviation);
    }

    private static void transformModel(PoseStack pose, ItemDisplayContext mode) {
        pose.translate(0.5, 0.5, 0);
        switch (mode) {
            case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> {
                pose.translate(0, 0, 0.53);
                pose.scale(0.6F, 0.6F, 0.6F);
            }
            case FIRST_PERSON_LEFT_HAND -> {
                pose.translate(0, 0.02, 0.56);
                pose.mulPose(Axis.YN.rotationDegrees(10));
                pose.scale(0.75F, 0.75F, 0.75F);
            }
            case FIRST_PERSON_RIGHT_HAND -> {
                pose.translate(0, 0.10, 0.56);
                pose.mulPose(Axis.YP.rotationDegrees(10));
                pose.scale(0.75F, 0.75F, 0.75F);
            }
            case HEAD -> {
                pose.mulPose(Axis.YP.rotationDegrees(180));
                pose.translate(0, -0.75, -0.75);
            }
            case GROUND -> {
                pose.translate(0, -0.10, 0.5);
                pose.scale(0.8F, 0.8F, 0.8F);
            }
            case FIXED -> {
                pose.mulPose(Axis.YN.rotationDegrees(180));
                pose.translate(0.5, 0.5, 0);
            }
            default -> { }
        }
    }

    private record RenderData(ItemStackRenderState model, ItemDisplayContext mode, String abbreviation) { }

    private static class Renderer implements SpecialModelRenderer<RenderData> {
        @Override
        public void submit(RenderData data, PoseStack pose, SubmitNodeCollector collector,
                           int light, int overlay, boolean foil, int outline) {
            boolean gui = data.mode() == ItemDisplayContext.GUI;
            boolean frame = data.mode() == ItemDisplayContext.FIXED;
            pose.pushPose();
            transformModel(pose, data.mode());
            data.model().submit(pose, collector, gui ? 0xF000F0 : light, gui ? 655360 : overlay, outline);
            pose.popPose();
            if (gui || frame) {
                pose.pushPose();
                pose.translate(0.5, 0.5, 0);
                // Preserve the original renderer's rotation (specified in radians).
                pose.mulPose(Axis.XN.rotation(180));
                pose.translate(-0.16, 0, -0.55);
                pose.scale(0.05F, 0.08F, 0.08F);
                if (frame) {
                    pose.mulPose(Axis.YN.rotationDegrees(180));
                    pose.mulPose(Axis.XN.rotationDegrees(53));
                    pose.translate(-8, -1, 1.7);
                    pose.scale(1, 0.65F, 1);
                }
                collector.submitText(pose, -5, 0, Component.literal(data.abbreviation()).getVisualOrderText(),
                        true, Font.DisplayMode.NORMAL, 0xF000F0, 0xFFFFFFFF, 0, 0);
                pose.popPose();
            }
        }

        @Override
        public void getExtents(Consumer<Vector3fc> output) { }

        @Override
        public RenderData extractArgument(ItemStack stack) { return null; }
    }

    public record Unbaked(ItemModel.Unbaked base) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemModels.CODEC.fieldOf("base").forGetter(Unbaked::base)).apply(instance, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() { return MAP_CODEC; }

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) { base.resolveDependencies(resolver); }

        @Override
        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
            return new ElementRenderer(base.bake(context, transformation));
        }
    }
}
