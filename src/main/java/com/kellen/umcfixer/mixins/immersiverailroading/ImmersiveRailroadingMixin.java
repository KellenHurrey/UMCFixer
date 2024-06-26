package com.kellen.umcfixer.mixins.immersiverailroading;

import cam72cam.immersiverailroading.ConfigSound;
import cam72cam.immersiverailroading.IRBlocks;
import cam72cam.immersiverailroading.IRItems;
import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.immersiverailroading.entity.*;
import cam72cam.immersiverailroading.gui.overlay.GuiBuilder;
import cam72cam.immersiverailroading.library.GuiTypes;
import cam72cam.immersiverailroading.library.KeyTypes;
import cam72cam.immersiverailroading.library.Particles;
import cam72cam.immersiverailroading.model.StockModel;
import cam72cam.immersiverailroading.net.KeyPressPacket;
import cam72cam.immersiverailroading.registry.DefinitionManager;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import cam72cam.immersiverailroading.render.SmokeParticle;
import cam72cam.immersiverailroading.render.block.RailBaseModel;
import cam72cam.immersiverailroading.render.item.*;
import cam72cam.immersiverailroading.render.multiblock.MBBlueprintRender;
import cam72cam.immersiverailroading.render.multiblock.TileMultiblockRender;
import cam72cam.immersiverailroading.render.rail.RailPreviewRender;
import cam72cam.immersiverailroading.tile.TileMultiblock;
import cam72cam.immersiverailroading.tile.TileRail;
import cam72cam.immersiverailroading.tile.TileRailGag;
import cam72cam.immersiverailroading.tile.TileRailPreview;
import cam72cam.mod.MinecraftClient;
import cam72cam.mod.ModEvent;
import cam72cam.mod.entity.Entity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.event.ClientEvents;
import cam72cam.mod.input.Keyboard;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.*;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.sound.Audio;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.function.Function;

@Mixin(value = ImmersiveRailroading.class, remap = false)
public class ImmersiveRailroadingMixin {

    /**
     * @author kellen
     * @reason to fix a crash
     */
    @Overwrite
    public void clientEvent(ModEvent event){
        switch (event) {
            case CONSTRUCT:
                BlockRender.register(IRBlocks.BLOCK_RAIL, RailBaseModel::getModel, TileRail.class);
                BlockRender.register(IRBlocks.BLOCK_RAIL_GAG, RailBaseModel::getModel, TileRailGag.class);
                BlockRender.register(IRBlocks.BLOCK_RAIL_PREVIEW, RailPreviewRender::render, TileRailPreview.class);
                BlockRender.register(IRBlocks.BLOCK_MULTIBLOCK, TileMultiblockRender::render, TileMultiblock.class);
                ItemRender.register(IRItems.ITEM_PLATE, new PlateItemModel());
                ItemRender.register(IRItems.ITEM_AUGMENT, new RailAugmentItemModel());
                ItemRender.register(IRItems.ITEM_RAIL, new RailItemRender());
                ItemRender.register(IRItems.ITEM_CAST_RAIL, new RailCastItemRender());
                ItemRender.register(IRItems.ITEM_TRACK_BLUEPRINT, new TrackBlueprintItemModel());
                ItemRender.register(IRItems.ITEM_ROLLING_STOCK_COMPONENT, new StockItemComponentModel());
                ItemRender.register(IRItems.ITEM_ROLLING_STOCK, new StockItemModel());
                ItemRender.register(IRItems.ITEM_LARGE_WRENCH, ObjItemRender.getModelFor(new Identifier("immersiverailroading", "models/item/wrench/wrench.obj"), new Vec3d(0.5, 0.0, 0.5), 2.0F));
                ItemRender.register(IRItems.ITEM_CONDUCTOR_WHISTLE, ObjItemRender.getModelFor(new Identifier("immersiverailroading", "models/item/whistle.obj"), new Vec3d(0.5, 0.75, 0.5), 0.1F));
                ItemRender.register(IRItems.ITEM_GOLDEN_SPIKE, ObjItemRender.getModelFor(new Identifier("immersiverailroading", "models/item/goldenspike/goldenspike.obj"), new Vec3d(0.5, 0.5, 0.5), 0.1F));
                ItemRender.register(IRItems.ITEM_HOOK, ObjItemRender.getModelFor(new Identifier("immersiverailroading", "models/item/brake_stick.obj"), new Vec3d(0.5, 0.0, 0.5), 2.0F));
                ItemRender.register(IRItems.ITEM_SWITCH_KEY, ObjItemRender.getModelFor(new Identifier("immersiverailroading", "models/item/switch_key/switch_key.obj"), new Vec3d(0.5, 0.0, 0.5), 1.0F));
                ItemRender.register(IRItems.ITEM_PAINT_BRUSH, ObjItemRender.getModelFor(new Identifier("immersiverailroading", "models/item/paint_brush.obj"), new Vec3d(0.5, 0.25, 0.5), 3.0F));
                ItemRender.register(IRItems.ITEM_RADIO_CONTROL_CARD, new Identifier("immersiverailroading", "items/radio_card"));
                ItemRender.register(IRItems.ITEM_MANUAL, new Identifier("immersiverailroading", "items/engineerslexicon"));
                ItemRender.register(IRItems.ITEM_TRACK_EXCHANGER, new TrackExchangerModel());
                IEntityRender<EntityMoveableRollingStock> stockRender = new IEntityRender<EntityMoveableRollingStock>() {
                    public void render(EntityMoveableRollingStock entity, RenderState state, float partialTicks) {
                        StockModel<?, ?> renderer = entity.getDefinition().getModel();
                        if (renderer != null) {
                            renderer.renderEntity(entity, state, partialTicks);
                        }

                    }

                    public void postRender(EntityMoveableRollingStock entity, RenderState state, float partialTicks) {
                        StockModel<?, ?> renderer = entity.getDefinition().getModel();
                        if (renderer != null) {
                            renderer.postRenderEntity(entity, state, partialTicks);
                        }

                    }
                };
                EntityRenderer.register(LocomotiveSteam.class, stockRender);
                EntityRenderer.register(LocomotiveDiesel.class, stockRender);
                EntityRenderer.register(CarPassenger.class, stockRender);
                EntityRenderer.register(CarFreight.class, stockRender);
                EntityRenderer.register(CarTank.class, stockRender);
                EntityRenderer.register(Tender.class, stockRender);
                EntityRenderer.register(HandCar.class, stockRender);
                Function<KeyTypes, Runnable> onKeyPress = (type) -> {
                    return () -> {
                        (new KeyPressPacket(type)).sendToServer();
                    };
                };
                Keyboard.registerKey("ir_keys.increase_throttle", Keyboard.KeyCode.NUMPAD8, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.THROTTLE_UP));
                Keyboard.registerKey("ir_keys.zero_throttle", Keyboard.KeyCode.NUMPAD5, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.THROTTLE_ZERO));
                Keyboard.registerKey("ir_keys.decrease_throttle", Keyboard.KeyCode.NUMPAD2, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.THROTTLE_DOWN));
                Keyboard.registerKey("ir_keys.increase_reverser", Keyboard.KeyCode.NUMPAD9, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.REVERSER_UP));
                Keyboard.registerKey("ir_keys.zero_reverser", Keyboard.KeyCode.NUMPAD6, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.REVERSER_ZERO));
                Keyboard.registerKey("ir_keys.decrease_reverser", Keyboard.KeyCode.NUMPAD3, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.REVERSER_DOWN));
                Keyboard.registerKey("ir_keys.increase_brake", Keyboard.KeyCode.NUMPAD7, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.TRAIN_BRAKE_UP));
                Keyboard.registerKey("ir_keys.zero_brake", Keyboard.KeyCode.NUMPAD4, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.TRAIN_BRAKE_ZERO));
                Keyboard.registerKey("ir_keys.decrease_brake", Keyboard.KeyCode.NUMPAD1, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.TRAIN_BRAKE_DOWN));
                Keyboard.registerKey("ir_keys.increase_independent_brake", Keyboard.KeyCode.NUMPAD7, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.INDEPENDENT_BRAKE_UP));
                Keyboard.registerKey("ir_keys.zero_independent_brake", Keyboard.KeyCode.NUMPAD4, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.INDEPENDENT_BRAKE_ZERO));
                Keyboard.registerKey("ir_keys.decrease_independent_brake", Keyboard.KeyCode.NUMPAD1, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.INDEPENDENT_BRAKE_DOWN));
                Keyboard.registerKey("ir_keys.horn", Keyboard.KeyCode.NUMPADENTER, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.HORN));
                Keyboard.registerKey("ir_keys.dead_mans_switch", Keyboard.KeyCode.MULTIPLY, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.DEAD_MANS_SWITCH));
                Keyboard.registerKey("ir_keys.start_stop_engine", Keyboard.KeyCode.ADD, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.START_STOP_ENGINE));
                Keyboard.registerKey("ir_keys.bell", Keyboard.KeyCode.SUBTRACT, "key.categories.immersiverailroading", (Runnable)onKeyPress.apply(KeyTypes.BELL));
                Keyboard.registerKey("ir_keys.config", Keyboard.KeyCode.DIVIDE, "key.categories.immersiverailroading", () -> {
                    GuiTypes.CONFIG.open(MinecraftClient.getPlayer());
                });
                Audio.setSoundChannels(ConfigSound.customAudioChannels);
            case INITIALIZE:
            case FINALIZE:
            default:
                break;
            case SETUP:
                GlobalRender.registerItemMouseover(IRItems.ITEM_TRACK_BLUEPRINT, TrackBlueprintItemModel::renderMouseover);
                GlobalRender.registerItemMouseover(IRItems.ITEM_MANUAL, MBBlueprintRender::renderMouseover);
                GlobalRender.registerOverlay((state, pt) -> {
                    // Player can sometimes be null causing the crash, usually when traveling through dimensions
                    Player player = MinecraftClient.getPlayer();
                    if (player == null) return;
                    Entity riding = player.getRiding();
                    if (riding instanceof EntityRollingStock) {
                        EntityRollingStock stock = (EntityRollingStock)riding;
                        if (stock.getDefinition().getOverlay() != null) {
                            stock.getDefinition().getOverlay().render(state, stock);
                        }

                    }
                });
                ClientEvents.MOUSE_GUI.subscribe((evt) -> {
                    if (!MinecraftClient.isReady()) {
                        return true;
                    } else {
                        Player player = MinecraftClient.getPlayer();
                        if (player == null) return true;
                        Entity riding = player.getRiding();

                        if (!(riding instanceof EntityRollingStock)) {
                            return true;
                        } else {
                            EntityRollingStock stock = (EntityRollingStock)riding;
                            return stock.getDefinition().getOverlay() != null ? stock.getDefinition().getOverlay().click(evt, stock) : true;
                        }
                    }
                });
                ClientEvents.TICK.subscribe(GuiBuilder::onClientTick);
                ClientEvents.TICK.subscribe(EntityRollingStockDefinition.ControlSoundsDefinition::cleanupStoppedSounds);
                Particles.SMOKE = Particle.register(SmokeParticle::new, SmokeParticle::renderAll);
                ClientPartDragging.register();
                break;
            case RELOAD:
                DefinitionManager.initDefinitions();
        }
    }
}
