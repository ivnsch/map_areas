map_areas
=========


Android maps v2 circles library


![ScreenShot](https://raw.github.com/i-schuetz/map_areas/master/screen1.png)


![ScreenShot](https://raw.github.com/i-schuetz/map_areas/master/screen2.png)


## Features:

- Create, move and resize map circles using gestures.

- Customize circle colors and border size.

- Possible to set min and max radius.

- Possible to set initial radius with meters or pixels.

- Possible to set custom drawables for markers or use default maps markers.

- Combinable with progressbar.

- Callbacks for diverse events (create circle, start resize, end resize, start move, end move, reached min/max radius).
Each event parameter delivers circle with geocoordinates and radius. Circle can be accessed / modified in listener.

- Customizable / extendable.




## Requirements:

Follow the instructions under:

https://developers.google.com/maps/documentation/android/start#getting_the_google_maps_android_api_v2

to configure the application using this library correctly.


The demo project requires a valid maps v2 api key.


## Possible improvements:

- Avoid mercator projection distortion when far away from equator (clearly noticeable on low zoom levels).


--------------------------------------------------------------------------------
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


