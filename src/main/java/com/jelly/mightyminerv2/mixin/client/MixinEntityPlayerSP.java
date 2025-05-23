package com.jelly.mightyminerv2.mixin.client;

import com.jelly.mightyminerv2.event.MotionUpdateEvent;
import com.jelly.mightyminerv2.macro.MacroManager;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPlayerSP.class, priority = Integer.MAX_VALUE)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Unique
    float mightyMinerv2$serverYaw = 0f;
    @Unique
    float mightyMinerv2$serverPitch = 0f;
    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    public void onUpdateWalkingPlayerPRE(CallbackInfo ci) {
        MotionUpdateEvent event = new MotionUpdateEvent(this.rotationYaw, this.rotationPitch);
        MinecraftForge.EVENT_BUS.post(event);
        this.mightyMinerv2$serverYaw = event.yaw;
        this.mightyMinerv2$serverPitch = event.pitch;
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationYaw:F", opcode = Opcodes.GETFIELD))
    public float onUpdateWalkingPlayerYaw(EntityPlayerSP instance) {
        return this.mightyMinerv2$serverYaw;
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationPitch:F", opcode = Opcodes.GETFIELD))
    public float onUpdateWalkingPlayerPitch(EntityPlayerSP instance) {
        return this.mightyMinerv2$serverPitch;
    }

    @Inject(method = "dropOneItem", at = @At("HEAD"), cancellable = true)
    public void onDropOneItem(boolean dropAll, CallbackInfoReturnable<EntityItem> cir) {
        if (MacroManager.getInstance().isRunning()) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}
