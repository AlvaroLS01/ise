package com.comerzzia.bimbaylola.pos.util.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.adapters.XmlAdapter;
  
public final class MyMapAdapter extends XmlAdapter<MyMapType,Map<String, String>>{
  
   @Override
   public MyMapType marshal(Map<String, String> arg0) throws Exception{
      MyMapType myMapType = new MyMapType();
      for(Entry<String, String> entry : arg0.entrySet()){
         MyMapEntryType myMapEntryType =  new MyMapEntryType();
         myMapEntryType.key = entry.getKey();
         myMapEntryType.value = entry.getValue();
         myMapType.entry.add(myMapEntryType);
      }
      
      return myMapType;
   }
  
   @Override
   public Map<String, String> unmarshal(MyMapType arg0) throws Exception{
      HashMap<String, String> hashMap = new HashMap<String, String>();
      for(MyMapEntryType myEntryType : arg0.entry){
         hashMap.put(myEntryType.key, myEntryType.value);
      }
      
      return hashMap;
   }
  
}
