# Doodle-App
Basic drawing app that was made in Android Studio. The Code folder is a short cut to the code I wrote.
### Features:
* Drawing on multiple layers
* rgba color selector
* Brush size and hardness settings
* Save images as PNG's

### Code

* **Brush:** Stores the settings for the brush, some examples would be color and brush size.

* **BrushActivity:** Interface for user to change the settings of the brush.

* **ColorActivity:** Interface for users to change the color of the brush.

* **Layer2:** Stores and displays a bitmap onto a canvas.

* **DrawingLayer:** Temporary stores the path of the brush until finger is lifted. Then paints the path to currently selected Layer2's bitmap using the brushes settings.

* **LayerInterface:** Interface to add, delete and rearrange the order of the layers. Stores list of all layers.

* **MainActivity:** Stores list of visible layers and displays them to the user

* **UpdateTimer:** Invalidates the the layer so DrawingLayer adds more points to the path


#### TODO/Fix:
* Refactor it
* Save images as a file to keep layers
* Open saved files
* Add fill tool
* Add select tool
* Add select UI
