#!/bin/bash

# Function to handle cleanup on exit.
cleanup() {
    echo -e "\nScript interrupted. Exiting…"
    exit 1
}
trap cleanup SIGINT

# Ensure required tools are installed.
if ! command -v magick &> /dev/null || ! command -v ffmpeg &> /dev/null; then
    echo "ImageMagick and ffmpeg must be installed."
    exit 1
fi

# Check if the correct number of arguments is provided
if [[ "$#" -ne 4 || ( "$#" -gt 0 && ( "$1" == "-h" || "$1" == "--help" ) ) ]]; then
    echo -e "Usage: $0 <input_dir> <output_dir> <extension> <framerate>\n"

    echo " <input_dir>      Path to the folder containing all frames generated in blender."
    echo " <output_dir>     Path to the folder containing the generated results."
    echo " <extension>      The extension of the frames."
    echo " <framerate>      The framerate in which the animation has been rendered with."

    exit 1
fi

input_dir="$1"
output_dir="$2"
ext="$3"
frame_rate="$4"

# Check arguments for being valid.

if [ ! -d "$input_dir" ]; then
    echo "Error: $input_dir is not a valid directory."
    exit 1
else
    input_dir="${input_dir%/}"
fi

output_dir="${output_dir%/}"
if [ ! -d "$output_dir" ]; then
    mkdir "$output_dir"
fi

# Frame rate check
if ! [[ "$frame_rate" =~ ^[0-9]+(\.[0-9]+)?$ ]]; then
    echo "Error: Frame rate must be a positive number."
    exit 1
fi

################################################################################

# Sort PNG files
IFS=$'\n' sorted_files=($(ls "$input_dir"/*."$ext" | sort))
unset IFS

# Handle static variants
first_frame="${sorted_files[0]}"

echo -ne "Creating static variants of icon (256x256px)…\r"
magick "$first_frame" -resize 256x256 "$output_dir"/icon256.png

echo -ne "Creating static variants of icon (512x512px)…\r"
magick "$first_frame" -resize 512x512 -define webp:lossless=true "$output_dir"/icon512.webp
magick "$output_dir"/icon256.png -fuzz 10% -fill none -draw "color 0,0 floodfill" -alpha on "$output_dir"/icon256.png

echo -ne "Creating static variants of icon (full res)…\r"
magick "$first_frame" -define webp:lossless=true "$output_dir"/icon.webp
magick "$output_dir"/icon.webp -fuzz 10% -fill none -draw "color 0,0 floodfill" -alpha on "$output_dir"/icon.webp

echo -e "Created static variants.\033[K"

# Handle animated variant using ffmpeg
echo -ne "Creating animated variant…\r"

ffmpeg -y -framerate "$frame_rate" -pattern_type glob -i "$input_dir/*.$ext" \
    -vf "scale=512:512" -c:v libwebp -q:v 90 -loop 0 $output_dir/animated.webp &> /dev/null

echo -e "Animated variant created.\033[K"
echo ""
echo "All icon variants can be found at $output_dir"
