# Single Threaded Ray Tracer
![Java 8](https://img.shields.io/badge/Java-1.8u92-blue.svg)

An adaptation of my earlier Swift 2.3 and Python 3.6 Ray tracer in Java 1.8 for use with the Texas A&M University class CSCE 435.

## About Ray Tracing
[Ray Tracing](https://en.wikipedia.org/wiki/Ray_tracing_(graphics)) is the name for a complex and thorough image synthesis process. Ray tracing generally involves many complex calculations and approximations of the way light behaves in a system. Often a single pixel is the result of hundreds of samples, each of which recurse and propagate multiple times, making the result look accurate. This accuracy comes with a very high price, long calculations. Because of this, it's generally favorable to build implementations in languages that can optimize for speed. Python is not one of those languages, but it is a very capable language, and the parameters of the scene we'll be tracing will be relatively simple.

## Instructions
The ray tracer should work without any changes, but its single-threaded performance will be slow.

### Running a sample on the TAMU Supercomputer
This has yet to be written.

## Crash course on why ray tracing is so slow
Without having to read the code, here's where all the time goes. The ray tracer will set up the scene, then it will begin to cast rays. for each pixel, it casts the number of rays specified by `samples` with small random offsets to prevent aliasing. Each cast ray then checks every object in the scene for an intersection, and then picks the nearest one. It then either reflects or refracts, casting another ray. This ray casting is recusive and can happen up to `depth` times unless the ray does not hit anything.

### Put in perspective
Lets say a hypothetical scene is 1920 pixels wide, by 1080 pixels tall. We might use a rough sampling value of 80, so the number of samples will be 1920 * 1080 * 80 = 165,888,000. Each sample will cast a ray, and bounce up to 10 times, requiring a new cast each time. Each ray involves calculating the intersection point with every object in the scene (5 quadrics and 1 plane), and then finding the closest. It then calculates other information needed to keep going.

These several million calculations took that same run 284 seconds or 4:44. Relatively speaking... that's slow. Most CPUs today can do billions of calculations per second, on one thread. A powerful GPU in a modern video game can blow that out of the water.

#### Okay, but why?
2 Main reasons:
* This code is single-threaded.
* Ray tracing techniques are *VERY* expensive compared to modern rasterization techniques (depth buffering).
  * Those very techniques are responsible for how easy it would be to implement new features, and how good they look.
There are two more reasons why it's slower: this code makes very little use of [localities](https://en.wikipedia.org/wiki/Locality_of_reference) , (which is something a compiler might help with), and I've opted for a recursive approach for the actual tracing, (as opposed to an iterative one).

#### Can it go faster?
Absolutely. There are at least 3 fundamentally different ways I can think of to multi-thread the application. This is actually quite open-ended.