## JOSM filter grammar

### Basics

A simple literal value will be searched for in tag keys and values and will match if it is a sub-string of either. If "regexp" mode is enabled, literals will be interpreted as regular expressions.

Example:

   _roof_ will match with
   
    parking=rooftop
    location=roof
    roof:colour=black
        
If the literal value contains white space (that is spaces, tabs etc) or characters and strings that have a special meaning for this grammar then you need to enclose the literal value in double quotes.

Example:

   _roof:shape_ will not match with
   
    roof:shape=dome
        
  however _"roof:shape"_ will match with the tag.
  
  
Literal values can be combined with **=**, **~**, **>** and **<** to match with specific key value combinations.

   _key_**=**_value_     will match exactly a tag with the key and value literals.

   _key_**=**            will match a tag with the literal key and an empty value  

   _key_**=**__*__       will match a tag with the literal key and any value (except in regexp mode)

   __*__**=**_value_     will match a tag with any key that has the literal value
   
   _regexp_**~**_regexp_ will interpret the literals as regular expressions and attempt to match them with the tags.

   _key_**>**_literal_   match if the tag value for the literal key is larger than the literal value supplied, vice versa for **<**
   
Substrings of values for a specific key can be found with the syntax _key_**:**_substring_ .

Example:

  _name_**:**_est_ will match
  
    name=best
  
  and 
  
    name=testing 
  
   
### Boolean expressions

White space between expressions is interpreted as a logical "and", just as the literal **AND** (case is ignored).

Examples:

   _roof_ _parking_ will match with
   
        parking=rooftop
        
 but not with 
 
        location=roof
        
 just as for _roof_ **AND** _parking_ 
        

**|** or **OR** are interpreted as a logical "or".

Example:

   _roof_ **OR** _parking_ will match with
   
        parking=rooftop

**-** is interpreted as a logical *not".

Examples:

   __-__roof will not match with
   
        rooftop
       
  but will match with
  
        parking

Expressions can be grouped with parentheses **(** **)**.

Example

  TBD  

### Element type, state and meta data 

  **type:node**             element is a node
  
  **type:way**              element is a way
  
  **type:relation**         element is a relation

  **new**                   element is newly created
  
  **modified**              element has been modified
  
  **deleted**               element has been deleted

  **user:**_user_           last editor of element is _user_
  
  **id:**_id_               id of the element is _id_
  
  **version:**_version_     version of the element is _version_
  
  **timestamp:**_timestamp_ timestamp of the element is _timestamp_ , or in a range in the format _start_**/**_end_
  
### Other element properties

  **closed**            true if the element is a closed way
  
  **untagged**          true if the element has no tags 

  **selected**          true if the element is selected
  
  **incomplete**        true if not all child elements (that is relation members or way nodes) have been down loaded
  
  **preset:**_preset_   true if the element matches the preset item _preset_, if preset item is a preset group, true if it matches any preset item in the group

  **nodes:**_range_     the number of way nodes is in _range_
                        _range_ can be one, exact number or a range specification in the form _start_**-**_end_
                        
  Examples
  
  **nodes:**_7_ will match ways with 7 nodes
  
  **nodes:**__-7__ will match ways with up to 7 nodes
  
  **nodes:**__2-__ will match ways with at least 2 nodes
  
  **nodes:**__2-7__ will match ways with a node count from 2 to 7
  
  **ways:**_range_      the number of ways the node is a member of is in _range_ or the relation has a number of members that are ways in _range_

  **tags:**_range_      the number of tags on the element is in _range_

  **members:**_range_   the number of members of the relation is in _range_

  **areasize:**_range_  the size of the area in square meters is in _range_

  **waylength:**_range_ the length of the way in meters is in _range_

  **role:**_role_       the element is a member of a relation with the role _role_           

  **hasRole:**_role_    the relation has a member with role _role_  

  **nth:**_number_      true if the element is the n'th member of a relation or n'th way node

  **nth%:**_number_     true for every n'th member of a relation or every n'th way node 

### Search area selection

  **inview**               true if the element is currently partially or fully displayed
  
  **allinview**            true if the element is currently fully displayed 

  **indownloadedarea**     true if the element is partially or fully in the down loaded regions

  **allindownloadedarea**  true if the element is fully in the down loaded regions

### Relationship to other elements

  **child** _expression_   true if the element is a child (that is relation member or way node) of an element that matches _expression_
  
  **parent** _expression_  true is the element is a parent of an element (relation member or way node) of an element that matches _expression_

### Notes for specific implementations

#### Vespucci

Currently unsupported elements

**user**, **changeset**, **child**, **parent**, **areasize**, **nth:** and **nth%:** 
