# Using the charset via Create displays

CC: Tweaked has a special charset. It's based of a combination of different standards actually. The problem however is, that Minecraft and Create doesn't use this charset. While other mods barely handle this problem and grant the Computer raw access to the displays, CC:C Bridge converts the characters to something displayable.

## Legacy Formatting is disabled

While this works for of the characters easily, some can't be converted, like the section symbol (`§`), as [Notch's legacy text formatting system](https://minecraft.wiki/w/Formatting_codes) uses it. _(Players in Minecraft actually can't type it in game as well at any time!)_

## The CCCCset (CC:C Charset)

Because of this, some characters have been replaced. **They convert from and to Create or CC either direction to ensure an easy way to process symbols on any side.**

This is how the charset looks like on monitors and displays outside of CC:

```
 |0 1 2 3 4 5 6 7 8 9 A B C D E F
-+--------------------------------  
0|  ☺ ☻ ♥ ♦ ♣ ♠ ● ○     ♂ ♀   ♪ ♬
1|▶ ◀ ↕ ‼ ¶ ░ ▬ ↨ ⬆ ⬇ ➡ ⬅ ∟ ⧺ ▲ ▼
2|  ! " # $ % & ' ( ) * + , - . /
3|0 1 2 3 4 5 6 7 8 9 : ; < = > ?
4|@ A B C D E F G H I J K L M N O
5|P Q R S T U V W X Y Z [ \ ] ^ _
6|` a b c d e f g h i j k l m n o
7|p q r s t u v w x y z { | } ~ ▒
8|⠀ ⠁ ⠈ ⠉ ⠂ ⠃ ⠊ ⠋ ⠐ ⠑ ⠘ ⠙ ⠒ ⠓ ⠚ ⠛
9|⠄ ⠅ ⠌ ⠍ ⠆ ⠇ ⠎ ⠏ ⠔ ⠕ ⠜ ⠝ ⠖ ⠗ ⠞ ⠟
A|▓ ¡ ¢ £ ¤ ¥ ¦ █ ¨ © ª « ¬ ­ ® ¯
B|° ± ² ³ ´ µ ¶ · ¸ ¹ º » ¼ ½ ¾ ¿
C|À Á Â Ã Ä Å Æ Ç È É Ê Ë Ì Í Î Ï
D|Ð Ñ Ò Ó Ô Õ Ö × Ø Ù Ú Û Ü Ý Þ ß
E|à á â ã ä å æ ç è é ê ë ì í î ï
F|ð ñ ò ó ô õ ö ÷ ø ù ú û ü ý þ ÿ
```

The charsets are equal to each other by about ~98%.  
The 'blit' characters _(from \\128 to \\159)_ have been untouched, although they don't really work on Flip Displays and Nixie Tubes. They do work on Vanilla displays like Signs and Lecterns however.

### Differences

Those are the main differences that have been made to the chars:

- **\\016** (`§`) in CC changes to **\\u2591** (`░`) in Create
- **\\160** (` `) in CC changes to **\\u2591** (`▓`) in Create
- **\\167** (`§`) in CC changes to **\\u2591** (`█`) in Create

As the pattern here gives it away, this mainly affects the symbols used for legacy formatting and the dithered blocks used by Create.
This makes it possible to make the same progressbars as Create uses them.

## Example

The line `Power: ███▓░░░` in Create would convert to `Power: §§§ §§§` in CC. Keep in mind, that the second row of `§` is **\\016** and not **\\167** like the first row.  
Same goes the other way around as well.