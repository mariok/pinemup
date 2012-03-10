package net.sourceforge.pinemup.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public enum NoteColor {
   YELLOW((byte)0, "yellow", new Color(254, 255, 204), new Color(255, 255, 154)),
   GREEN((byte)1, "green", new Color(216, 255, 204), new Color(177, 255, 153)),
   BLUE((byte)2, "blue", new Color(204, 213, 255), new Color(153, 170, 255)),
   TURQUOISE((byte)3, "turquoise", new Color(225, 247, 250), new Color(177, 240, 253)),
   ORANGE((byte)4, "orange", new Color(252, 234, 177), new Color(252, 221, 126)),
   MAGENTA((byte)5, "magenta", new Color(255, 204, 230), new Color(255, 153, 204)),
   PURPLE((byte)6, "purple", new Color(222, 202, 252), new Color(192, 154, 255)),
   GRAY((byte)7, "gray", new Color(209, 209, 209), new Color(184, 184, 184)),
   RED((byte)8, "red", new Color(255, 166, 167), new Color(255, 115, 115));

   public static final NoteColor DEFAULT_COLOR = NoteColor.YELLOW;

   private byte code;
   private String name;
   private Color color1;
   private Color color2;

   private NoteColor(byte code, String name, Color color1, Color color2) {
      this.code = code;
      this.name = name;
      this.color1 = color1;
      this.color2 = color2;
   }

   public byte getCode() {
      return code;
   }

   public String getLocalizedName() {
      return name;
   }

   public Color getColor1() {
      return color1;
   }

   public Color getColor2() {
      return color2;
   }

   public static NoteColor getNoteColorByCode(byte code) {
      if (code >= 0 && code < NoteColor.values().length) {
         return NoteColor.values()[code];
      } else {
         return NoteColor.DEFAULT_COLOR;
      }
   }

   public static String[] getLocalizedColorNames() {
      List<String> names = new ArrayList<String>(NoteColor.values().length);
      for (NoteColor color : NoteColor.values()) {
         names.add(color.getLocalizedName());
      }
      return names.toArray(new String[NoteColor.values().length]);
   }
}
