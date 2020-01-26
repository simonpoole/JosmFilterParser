

# JosmFilterParser


## Usage

        try {
            JosmFilterParser parser = new JosmFilterParser(new ByteArrayInputStream(filterString.getBytes()));
            parser.condition().eval(...);
        } catch (ParseException pex) {
            ...
        } catch (Error err) {
            ...
        }
        
Your OSM elements object must either implement the Meta interface or be wrapped in an object that implements it. The object can then be passed to the _eval_ method.

## Supported expressions

This is what the parser currently supports not necessarily what can be supported in a specific application.

|    |Syntax                         | 
|--- |--- |
|- [x]| Baker Street                   | 
|- [x]| __"Baker Street"__             | 
|- [x]| _key_**:**_valuefragment_      |
|- [x]| **-**_key_**:**_valuefragment_ |
|- [x]| _key_                          | 
|- [x]| _key_**=**_value_              | 
|- [x]| *key*__=*__                    | 
|- [x]| _key_**=**                     | 
|- [x]| __*=__*value*                  | 
|- [ ]| _key_**>**_value_              | 
|- [ ]| _key_**<**_value_              | 
|- [x]|_expr_ _expr_                   |
|- [x]|_expr_ __&#124;__ _expr_        | 
|- [x]|_expr_ __OR__ _expr_            | 
|- [x]|__-__*expr*                     | 
|- [x]|__(__*expr*__)__                | 
|- [x]|__type:node__                       | 
|- [x]|__type:way__                        | 
|- [x]|__type:relation__                   | 
|- [x]|__closed__                          | 
|- [x]|__untagged__                        |
|- [ ]|__preset:"__preset item path__"__ | 
|- [ ]|__preset:"__preset group path/*__"__ | 
|- [x]| __user:__                    |
|- [x]|__id:__                       | 
|- [x]|__version:__                  |
|- [x]|__changeset:__                |
|- [x]|__timestamp:__                | 
|- [x]|__nodes:__*range*               |
|- [x]|__ways:__*range*                 |
|- [x]|__tags:__*range*               | 
|- [x]|__role:__*role*               |
|- [x]|__areasize:__*range*           | 
|- [x]|__waylength:__*range*          | 
|- [x]|__modified__                  | 
|- [x]|__new__                       | 
|- [ ]|__selected__                  |
|- [ ]|__incomplete__                | 
|- [x]|__deleted__                   | 
|- [ ]|__child__ _expr_              | 
|- [ ]|__parent__ _expr_             | 
|- [ ]|__hasRole:__*stop*            | 
|- [ ]|__nth:__*number*              | 
|- [ ]|__nth%:__*number*             |
|- [ ]|__inview__                    | 
|- [ ]|__allinview__                 | 
|- [ ]|__indownloadedarea__          | 
|- [ ]|__allindownloadedarea__       | 

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
	    compile 'ch.poole.osm:JosmFilterParser:0.0.0'
	    ...
	}
