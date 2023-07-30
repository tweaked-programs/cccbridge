# RedRouter Block

![Image title](../assets/images/peripherals/redrouter_block.png){ align=left width="100" }
This peripheral is provided by the **RedRouter Block**. It is used to control redstone signals.

The peripheral acts similar to the [Redstone API](https://tweaked.cc/module/redstone.html) with some exceptions like missing bundled cable support.  
The sides are configured similarly to the turtle, where `"left"` is **relative to the blocks facing.**

## Metadata

| | |
|-|-|
| Peripheral | v1 |
| Attach name | `"redrouter"` |

### Events

The RedRouter can send the following event:

| Name | Description | Parameter 1 |
|------|-------------|-------------|
| `"redstone"` | Whenever a redstone signal has changed. | `string`: **attatched_name** |

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