# RedRouter Block

![Image title](../assets/images/peripherals/redrouter_block.png){ align=right width="120" }

The **RedRouter** can be used to control redstone signals from further away without needing multiple computers or an bundled cables mod.

The API is almost identical to the [Redstone API](https://tweaked.cc/module/redstone.html) with some exceptions like missing bundled cable support.
The sides are configured similarly to the turtle, where `"left"` is **relative to the blocks facing.**

!!! info
    While the RedRouter is not marked as deprecated, it is recommended to check out the [Redstone Relay](https://tweaked.cc/peripheral/redstone_relay.html) instead, which CC: Tweaked introduced on their own in newer versions.

## Example

Using the RedRouter is straight forward, if one already knows how to use the redstone API:

```lua
local redrouter = peripheral.find("redrouter")

-- Make the RedRouter emit a redstone signal on full strength
-- from its left side.
redrouter.setOutput("left", true)

os.pullEvent("redstone") -- Wait until a redstone signal changed

-- Print a status update of the back sides redstone strength:
local strength = redrouter.getAnalogInput("back")
print("Something is sending an redstone signal with the strength "..strength.." from the redrouters back.")
```

Notice how it not only can send redstone outputs but also read inputs, both through booleans or analog values from 0 to 15.

!!! info
    `redrouter.getInput` returns the current value in the world, which might be an external source. `redrouter.getOutput` on the other hand returns what the redrouter got told to emit.

---

## Metadata

| | |
|-|-|
| Peripheral | v1 |
| Attach name | `"redrouter"` |
| Attach side | all |

### Events

The RedRouter can send the following event:

| Name | Description | Parameter 1 |
|------|-------------|-------------|
| `"redstone"` | [Whenever a redstone signal has changed.](https://tweaked.cc/event/redstone.html) | `string`: **attached_name** |

---

## Functions

### `setOutput(side, on)`
Set a redstone signal for a specific side.

**Parameters**

 1. `side` : [string](https://www.lua.org/manual/5.1/manual.html#5.4) The side to set.
 2. `on`: [boolean](https://www.lua.org/manual/5.1/manual.html#2.2) The signals state `true` _(power 15)_ or `false` _(power 0)_

---

### `setAnalogOutput(side, value)`
Set a redstone signal strength for a specific side.

**Parameters**

 1. `side` : [string](https://www.lua.org/manual/5.1/manual.html#5.4) The side to set.
 2. `value` : [number](https://www.lua.org/manual/5.1/manual.html#2.2) The signal strength between 0 and 15.

**Throws**

 * Whenever `value` is not between 0 and 15.

---

### `getOutput(side)`
Get the current redstone output of a specific side. *(see `setOutput`)*

**Parameters**

 1. `side`: [string](https://www.lua.org/manual/5.1/manual.html#5.4) The side to get.

**Returns**

 1. `boolean` Whether the redstone output is on or off.

---

### `getInput(side)`
Get the current redstone input of a specific side.

**Parameters**

 1. `side`: [string](https://www.lua.org/manual/5.1/manual.html#5.4) The side to get.

**Returns**

 1. `boolean` Whether the redstone input is on or off.

---

### `getAnalogOutput(side)`
Get the redstone output signal strength for a specific side.  *(see `setAnalogOutput`)*

**Parameters**

 1. `side`: [string](https://www.lua.org/manual/5.1/manual.html#5.4) The side to get.

**Returns**

 1. [number](https://www.lua.org/manual/5.1/manual.html#2.2) The output signal strength, between 0 and 15.

---

### `getAnalogInput(side)`
Get the redstone input signal strength for a specific side.

**Parameters**

 1. `side`: [string](https://www.lua.org/manual/5.1/manual.html#5.4) The side to get.

**Returns**

 1. [number](https://www.lua.org/manual/5.1/manual.html#2.2) The input signal strength, between 0 and 15.

---
