
This is work in progress do not use yet

# JosmFilterParser


## Usage

    try
	 {
	     	JosmFilterParser parser = new JosmFilterParser(new ByteArrayInputStream(line.getBytes()));
			List<Restriction> list = parser.restrictions();
		  ...	
	 } catch ...

## Including in your project

You can either download the jar from github or add the following to your build.gradle

	...
	    repositories {
	        ...   
	        maven { url 'https://dl.bintray.com/simonpoole/osm' } 
	        ...              
	    }
	...
	
	dependencies {
	    ...
	    compile 'ch.poole:JosmFilterParser:0.2.3'
	    ...
	}
