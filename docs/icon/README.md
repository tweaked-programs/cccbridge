# Official Icon

The whole icon can be generated using the included `.blend` file. It works with Blender 4.X.

With Blender installed, the icon can be rendered using `blender -b ./icon.blend -a`, though, `-a` will render 120 frames for the full animation as seen on CurseForge. If this is not needed, leaving out `-a` or canceling the process after the first frame has been rendered is enough and significantly shortens the time and power used to render the results.

After generating the icon frame(s), the included converter script can be used to automatically generate all variants:

```sh
$ ./converter.sh -h
Usage: ./converter.sh <input_dir> <output_dir> <extension> <framerate>

 <input_dir>      Path to the folder containing all frames generated in blender.
 <output_dir>     Path to the folder containing the generated results.
 <extension>      The extension of the frames.
 <framerate>      The framerate in which the animation has been rendered with.
```

Here is an example: `./converter.sh /tmp ./out png 25`

> Note: This script requires ImageMagick and ffmpeg to be installed.
