<h1> ImagePicker Library </h1>

<p>This libarary helps you to select image form 📸camera ,🌄 gallery or 📂files , Enter the size you want to compress the image or skip it if you want the image in orignal size.</p>

<h1> How To Use ❓ </h1>
<P> 
-To compress the image to 2mb use the following.<br>
// Here 2 is for referance you can change it accordingly.

``` 
val intent = Intent(this,GetImage::class.java)
            intent.putExtra("size",2)
            startActivityForResult(intent,100)
```
-To get the orignal sized image. <br>
```
val intent = Intent(this,GetImage::class.java)
            startActivityForResult(intent,100)
```

-Handle Response 
```
if(resultCode == RESULT_OK){
            data?.data?.let {
                // here you get uri of the image you can manipulate it accordingly .
            }
        }
```

<h1> Implementation </h1>

```xml
<repositories>
   <repository>
     <id>jitpack.io</id>
     <url>https://jitpack.io</url>
   </repository>
</repositories>
```
	Step 2. Add the dependency
```xml
<dependency>
    <groupId>com.github.Aniket752</groupId>
    <artifactId>ImagePicker</artifactId>
    <version>1.0.3</version>
</dependency>
```
gradle:
	
Add it in your root build.gradle at the end of repositories:
```groovy	
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```	
	Step 2. Add the dependency
```groovy	
implementation 'com.github.Aniket752:ImagePicker:1.0.3'
```
