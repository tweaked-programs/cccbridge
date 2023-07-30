# Using Animatronics: The Analog Way

Animatronics are normally controlled by a Computer.
However, some people might not want to use _another Computer_, just to position it once. Others might not know how to use CC: Tweaked at all and can't use it because of this.

But the Animatronic **can actually be used without a Computer**.

## Viewing rotation

**To take a look at the current pose of an Animatronic**, simply run the following command in Minecraft:
```mcfunction
data get block <x> <y> <z>
```
_The coordinates here represent where the Animatronic is standing._

## Changing rotation

To change the rotation of a body part, you can run a similar command in Minecraft:
```mcfunction
data modify block <x> <y> <z> <body_part> set value [<rot_x>, <rot_y>, <rot_z>]
```

* _Again, the coordinates here represent where the Animatronic is standing._
* `<body_part>` is either `"leftArmPose"`, `"rightArmPose"`, `"bodyPose"` or `"headPose"`.
* `[<rot_x>, <rot_y>, <rot_z>]` is the new rotation of that body part.

**After entering this command, you can see how the Animatronic applies that position.**