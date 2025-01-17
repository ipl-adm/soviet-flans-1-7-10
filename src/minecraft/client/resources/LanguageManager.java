package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.Locale;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.util.StringTranslate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanguageManager implements IResourceManagerReloadListener {

   private static final Logger logger = LogManager.getLogger();
   private final IMetadataSerializer theMetadataSerializer;
   private String currentLanguage;
   protected static final Locale currentLocale = new Locale();
   private Map languageMap = Maps.newHashMap();


   public LanguageManager(IMetadataSerializer var1, String var2) {
      this.theMetadataSerializer = var1;
      this.currentLanguage = var2;
      I18n.setLocale(currentLocale);
   }

   public void parseLanguageMetadata(List var1) {
      this.languageMap.clear();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         IResourcePack var3 = (IResourcePack)var2.next();

         try {
            LanguageMetadataSection var4 = (LanguageMetadataSection)var3.getPackMetadata(this.theMetadataSerializer, "language");
            if(var4 != null) {
               Iterator var5 = var4.getLanguages().iterator();

               while(var5.hasNext()) {
                  Language var6 = (Language)var5.next();
                  if(!this.languageMap.containsKey(var6.getLanguageCode())) {
                     this.languageMap.put(var6.getLanguageCode(), var6);
                  }
               }
            }
         } catch (RuntimeException var7) {
            logger.warn("Unable to parse metadata section of resourcepack: " + var3.getPackName(), var7);
         } catch (IOException var8) {
            logger.warn("Unable to parse metadata section of resourcepack: " + var3.getPackName(), var8);
         }
      }

   }

   public void onResourceManagerReload(IResourceManager var1) {
      ArrayList var2 = Lists.newArrayList(new String[]{"en_US"});
      if(!"en_US".equals(this.currentLanguage)) {
         var2.add(this.currentLanguage);
      }

      currentLocale.loadLocaleDataFiles(var1, var2);
      StringTranslate.replaceWith(currentLocale.field_135032_a);
   }

   public boolean isCurrentLocaleUnicode() {
      return currentLocale.isUnicode();
   }

   public boolean isCurrentLanguageBidirectional() {
      return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
   }

   public void setCurrentLanguage(Language var1) {
      this.currentLanguage = var1.getLanguageCode();
   }

   public Language getCurrentLanguage() {
      return this.languageMap.containsKey(this.currentLanguage)?(Language)this.languageMap.get(this.currentLanguage):(Language)this.languageMap.get("en_US");
   }

   public SortedSet getLanguages() {
      return Sets.newTreeSet(this.languageMap.values());
   }

}
