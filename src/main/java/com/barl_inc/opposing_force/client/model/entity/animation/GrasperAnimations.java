package com.barl_inc.opposing_force.client.model.entity.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class GrasperAnimations {

	public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(4.0F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-34.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-46.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-34.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-45.2F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-66.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-45.2F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-63.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(-34.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3333F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(-46.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.8333F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-34.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5F, KeyframeAnimations.degreeVec(-46.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(-34.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-39.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-51.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-39.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-51.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(-39.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3333F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(-51.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7083F, KeyframeAnimations.degreeVec(-46.55F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7917F, KeyframeAnimations.degreeVec(-65.95F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.degreeVec(-40.76F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.9583F, KeyframeAnimations.degreeVec(-61.7F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-50.25F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-39.2F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5F, KeyframeAnimations.degreeVec(-51.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(-39.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-39.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-51.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-39.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-51.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(-39.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0833F, KeyframeAnimations.degreeVec(-59.8F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(-65.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3333F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(-51.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.8333F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-39.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5F, KeyframeAnimations.degreeVec(-51.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-42.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(-39.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-34.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-46.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-34.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-46.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(-34.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3333F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(-46.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-34.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0833F, KeyframeAnimations.degreeVec(-54.8F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.25F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5F, KeyframeAnimations.degreeVec(-46.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-43.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-37.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(-34.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("hand_main", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(-4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3333F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.8333F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5F, KeyframeAnimations.degreeVec(4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.6667F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(-4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition MOVING = AnimationDefinition.Builder.withLength(1.0F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 1.73F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 1.73F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 1.73F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, 1.73F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(70.0F, -34.64F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(50.0F, -34.64F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(70.0F, -34.64F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(50.0F, -34.64F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.87F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.87F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.87F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.87F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-36.34F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-36.34F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-53.66F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-53.66F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-36.34F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-36.34F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-53.66F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-53.66F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 1.0F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 1.0F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-53.66F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-53.66F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-36.34F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-36.34F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-53.66F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-53.66F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-36.34F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-36.34F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 1.0F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 1.0F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 1.73F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 1.73F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 1.73F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 1.73F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(70.0F, 34.64F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(50.0F, 34.64F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(70.0F, 34.64F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(50.0F, 34.64F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.87F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.87F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.87F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, 0.87F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("base", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 3.46F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(4.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 3.46F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(2.0F, 0.0F, -3.46F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(4.0F, 0.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(2.0F, 0.0F, -3.46F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("base", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition LEAP_START = AnimationDefinition.Builder.withLength(1.0F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-85.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(75.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(60.0F, -20.0F, -20.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(60.0F, -20.0F, -20.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(65.0F, 1.5F, 65.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(60.0F, -20.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(65.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(75.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(60.0F, 20.0F, 20.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(60.0F, 20.0F, 20.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(65.0F, -1.5F, -65.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(60.0F, 20.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("hand_main", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition LEAP_LOOP = AnimationDefinition.Builder.withLength(1.0F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-85.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-89.33F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-89.33F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-85.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-82.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-80.67F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-80.67F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-82.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-85.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-89.33F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-89.33F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-85.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-82.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-80.67F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-80.67F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-82.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-85.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(60.0F, -20.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(60.0F, -24.33F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(60.0F, -24.33F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(60.0F, -15.67F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(60.0F, -15.67F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(60.0F, -24.33F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(60.0F, -24.33F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(60.0F, -15.67F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(60.0F, -15.67F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(60.0F, -24.33F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(60.0F, -24.33F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(60.0F, -15.67F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(60.0F, -15.67F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(60.0F, -24.33F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(60.0F, -24.33F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(60.0F, -15.67F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(60.0F, -15.67F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(60.0F, -20.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-34.33F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-25.67F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(60.0F, 20.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(60.0F, 24.33F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(60.0F, 24.33F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(60.0F, 15.67F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(60.0F, 15.67F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(60.0F, 24.33F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(60.0F, 24.33F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(60.0F, 15.67F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(60.0F, 15.67F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(60.0F, 24.33F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(60.0F, 24.33F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(60.0F, 15.67F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(60.0F, 15.67F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(60.0F, 24.33F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(60.0F, 24.33F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(60.0F, 15.67F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(60.0F, 15.67F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(60.0F, 20.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition SLAP = AnimationDefinition.Builder.withLength(1.0F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(150.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(150.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.375F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4583F, KeyframeAnimations.scaleVec(1.0F, 3.0F, 1.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.scaleVec(1.0F, 3.0F, 1.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(15.0F, -15.0F, -55.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(15.0F, -15.0F, -55.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-7.5F, 30.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-7.5F, 30.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(15.0F, 15.0F, 55.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(15.0F, 15.0F, 55.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-7.5F, -30.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-7.5F, -30.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition PUNCH = AnimationDefinition.Builder.withLength(1.0F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(75.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(75.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(22.581F, 36.7164F, 102.3719F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(22.581F, 36.7164F, 102.3719F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(22.581F, -36.7164F, -102.3719F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(22.581F, -36.7164F, -102.3719F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition FLINGED = AnimationDefinition.Builder.withLength(1.0F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-85.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-145.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-205.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-265.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-325.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-385.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-445.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-505.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-565.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-625.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-685.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-745.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-805.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-42.01F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-42.01F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-67.99F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-67.99F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-42.01F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-42.01F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-67.99F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-67.99F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, -0.25F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.25F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, -0.25F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.25F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(60.0F, -20.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(60.0F, -20.0F, 1.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(60.0F, -20.0F, 1.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(60.0F, -20.0F, -41.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(60.0F, -20.0F, -41.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(60.0F, -20.0F, 1.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(60.0F, -20.0F, 1.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(60.0F, -20.0F, -41.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(60.0F, -20.0F, -41.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(60.0F, -20.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-72.99F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-72.99F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-47.01F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-47.01F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-72.99F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-72.99F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-47.01F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-47.01F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.5F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -0.5F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.5F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -0.5F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.5F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-47.01F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-47.01F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-72.99F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-72.99F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-47.01F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-47.01F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-72.99F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-72.99F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, -0.6F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.6F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, -0.6F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -1.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, -0.6F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.6F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, -0.6F, -0.87F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -1.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-67.99F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-67.99F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-42.01F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-42.01F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-67.99F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-67.99F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-42.01F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-42.01F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.5F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -0.5F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.5F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -0.5F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, -0.25F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.5F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(60.0F, 20.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(60.0F, 20.0F, 41.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(60.0F, 20.0F, 41.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(60.0F, 20.0F, -1.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(60.0F, 20.0F, -1.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(60.0F, 20.0F, 41.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(60.0F, 20.0F, 41.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8333F, KeyframeAnimations.degreeVec(60.0F, 20.0F, -1.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(60.0F, 20.0F, -1.65F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(60.0F, 20.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition STUN = AnimationDefinition.Builder.withLength(4.0F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(41.5F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(43.25F, -3.03F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(46.75F, -1.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(48.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(46.75F, 1.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(43.25F, 3.03F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(41.5F, 3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3333F, KeyframeAnimations.degreeVec(43.25F, 3.03F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(46.75F, 1.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(48.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(46.75F, -1.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.6667F, KeyframeAnimations.degreeVec(43.25F, -3.03F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(41.5F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(20.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(22.5F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(27.5F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(30.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(27.5F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(22.5F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(20.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3333F, KeyframeAnimations.degreeVec(22.5F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(27.5F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(30.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(27.5F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.6667F, KeyframeAnimations.degreeVec(22.5F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(20.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(39.8F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(39.8F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(50.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(50.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3333F, KeyframeAnimations.degreeVec(39.8F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(39.8F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(50.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.6667F, KeyframeAnimations.degreeVec(50.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(48.03F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(48.03F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(41.97F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(41.97F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3333F, KeyframeAnimations.degreeVec(48.03F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(48.03F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(41.97F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.6667F, KeyframeAnimations.degreeVec(41.97F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(39.0F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(42.0F, -3.03F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(48.0F, -1.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(51.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(48.0F, 1.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(42.0F, 3.03F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(39.0F, 3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3333F, KeyframeAnimations.degreeVec(42.0F, 3.03F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(48.0F, 1.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(51.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(48.0F, -1.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.6667F, KeyframeAnimations.degreeVec(42.0F, -3.03F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(39.0F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(21.46F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(20.17F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(28.54F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(29.83F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(21.46F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3333F, KeyframeAnimations.degreeVec(20.17F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(28.54F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(29.83F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.6667F, KeyframeAnimations.degreeVec(26.29F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(21.46F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition FLIP_BACK = AnimationDefinition.Builder.withLength(1.5F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("index_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(41.5F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(75.0F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(75.0F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-15.0F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-15.0F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(20.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(50.0F, -35.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(50.0F, -35.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(125.0F, -35.0F, -95.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(125.0F, -35.0F, -95.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_left", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2917F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("middle_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 2.78F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("ring_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, -2.78F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("pinky_finger", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(39.0F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(75.0F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(75.0F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-15.0F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-15.0F, -3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(21.46F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(50.0F, 35.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(50.0F, 35.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(125.0F, 35.0F, 95.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(125.0F, 35.0F, 95.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("thumb_right", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2917F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("hand_main", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();
}
