package net.sourceforge.pinemup.ui;

public abstract class PinEmUpUI {
   private static PinEmUpUI ui;

   public static void setUI(PinEmUpUI ui) {
      PinEmUpUI.ui = ui;
   }

   public static PinEmUpUI getUI() {
      return ui;
   }

   public abstract void initialize();

   public abstract void refreshCategories();

   public abstract void refreshI18NStrings();
}
