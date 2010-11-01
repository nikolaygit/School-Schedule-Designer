Editor for .property files:



== Start the program in a specific language ==
In the RUN configuration, Arguments tab, "Program Arguments" section, add the following parameter:
-nl <lang_code>
Replace <lang_code> with your language code, for example "bg" for Bulgarian, "en" for English, "de" for German


=== Localization ===

Quote from http://www.ibm.com/developerworks/java/library/os-eclipse-globalrcp/index.html :
"If the properties file contains non-ASCII characters, 
such as the product name or about text, you need to convert the file 
from native encodings to ASCII with escaped Unicode. 
<JDK_HOME>\bin\native2ascii can be used to finish the conversion."

But the Eclipse ResourceBundle Editor can do this and even better: 
http://sourceforge.net/projects/eclipse-rbe/

That's why it is used for editing all language files!

=======================


