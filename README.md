

# JosmFilterParser


## Usage

        try {
            JosmFilterParser parser = new JosmFilterParser(new ByteArrayInputStream(filterString.getBytes()));
            boolean matches = parser.condition(false).eval(...);
        } catch (ParseException pex) {
            ...
        } catch (Error err) {
            ...
        }
        
Your OSM elements object must either implement the Meta interface or be wrapped in an object that implements it. The object can then be passed to the _eval_ method.

## Supported expressions

This is what the parser currently supports not necessarily what can be supported in a specific application.

|    |Syntax                          |              | 
|--- |------------------------------- |------------- |
|✅| Baker Street                        |              |
|✅| __"Baker Street"__                 |              | 
|✅| _key_**:**_valuefragment_          |              |
|✅| **-**_key_**:**_valuefragment_      |              |
|✅| _key_                              |              | 
|✅| _key_**=**_value_                  |              | 
|✅| *key*__=*__                         |              | 
|✅| _key_**=**                          |              | 
|✅| __*=__*value*                       |              | 
|✅| _key_**>**_value_                  |              | 
|✅| _key_**<**_value_                  |              | 
|✅|_expr_ _expr_                        |              |
|✅|_expr_ __&#124;__ _expr_             |              | 
|✅|_expr_ __OR__ _expr_                 |              | 
|✅|__-__*expr*                           |              | 
|✅|__(__*expr*__)__                      |              | 
|✅|__type:node__                        |              | 
|✅|__type:way__                         |              | 
|✅|__type:relation__                    |              | 
|✅|__closed__                           |              | 
|✅|__untagged__                         |              |
|✅|__preset:"__preset item path__"__    |              | 
|✅|__preset:"__preset group path/*__"__ |              | 
|✅|__user:__                            |              |
|✅|__id:__                              |              | 
|✅|__version:__                         |              |
|✅|__changeset:__                       |              |
|✅|__timestamp:__                       |              | 
|✅|__nodes:__*range*                    |              |
|✅|__ways:__*range*                     |              |
|✅|__members:__*range*                  | extension    |
|✅|__tags:__*range*                     |              | 
|✅|__role:__*role*                      |              |
|✅|__areasize:__*range*                 |              | 
|✅|__waylength:__*range*                |              | 
|✅|__modified__                         |              | 
|✅|__new__                              |              | 
|✅|__ways:__*range*                     |              |
|✅|__selected__                         |              |
|✅|__incomplete__                       |              | 
|✅|__deleted__                          |              | 
|✅|__child__ _expr_                    |              | 
|✅|__parent__ _expr_                   |              | 
|✅|__hasRole:__*role*                   |              | 
||__nth:__*number*                    |              | 
||__nth%:__*number*                   |              |
|✅|__inview__                           |              | 
|✅|__allinview__                        |              | 
|✅|__indownloadedarea__                 |              | 
|✅|__allindownloadedarea__              |              | 

Version 0.3.0 and later optionally supports regexps instead of the * wildcard.

## Including in your project

You can either download the jar from github or add the following to your build.gradle

	...
	    repositories {
	        ...   
	        jcenter()
	        ...              
	    }
	...
	
	dependencies {
	    ...
	    compile 'ch.poole.osm:JosmFilterParser:0.4.1'
	    ...
	}
