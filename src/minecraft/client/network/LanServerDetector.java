package net.minecraft.client.network;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanServerDetector {

   private static final AtomicInteger field_148551_a = new AtomicInteger(0);
   private static final Logger logger = LogManager.getLogger();


   // $FF: synthetic method
   static AtomicInteger access$000() {
      return field_148551_a;
   }

   // $FF: synthetic method
   static Logger access$100() {
      return logger;
   }

}
