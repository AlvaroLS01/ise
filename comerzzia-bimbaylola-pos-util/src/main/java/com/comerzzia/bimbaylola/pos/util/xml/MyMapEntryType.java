package com.comerzzia.bimbaylola.pos.util.xml;

import javax.xml.bind.annotation.XmlElement;
 
public class MyMapEntryType{
 
   @XmlElement(name="key")
   public String key; 
   @XmlElement(name="value")
   public String value;
 
}
