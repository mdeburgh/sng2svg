/** Render a song in SVG	. */
import java.io.*;
public class SVGRenderer {
	
  /* Page definitions */
  float MARGIN_X;
  float MARGIN_TOP;
  float MARGIN_BOTTOM;
  float PAGE_HEIGHT;
  float PAGE_WIDTH;
  
  /* Title definitions */
  int TITLE_FONT_SIZE;
  /* Bar layout */
  int BARS_PER_LINE;
  float BAR_LINE_WIDTH;
  float BAR_LINE_HEIGHT;
  float BAR_SPACING_X;
  float BAR_SPACING_Y;
  
  /* Chord and text layout */
  int CHORD_FONT_SIZE;
  int TEXT_FONT_SIZE;
  int REPEAT_FONT_SIZE;
  int BAR_DESC_FONT_SIZE;
  float BAR_DESC_MARGIN_X;
  float BAR_DESC_MARGIN_Y;
  float CHORD_MARGIN_X;
  float CHORD_HEIGHT_ABOVE_BAR;
  float TEXT_BELOW_BAR;
  float REPEAT_MARGIN_X;
  float REPEAT_MARGIN_Y;
  
  /* Current rendering co-ordinates */
  float x;
  float y;
  int bars_this_line;
  Writer out;
  String bar_desc_text;  /* Holds the string description of the bars block */
  
  public SVGRenderer(Writer outs)
  {
      /* Page definition */  
      MARGIN_X = 100.0f;
      MARGIN_TOP = 150.0f;
      MARGIN_BOTTOM = 90.0f; 
      PAGE_HEIGHT = 2970.0f;  
      PAGE_WIDTH = 2100.0f;
      
      /* Title definitions */
      TITLE_FONT_SIZE = 100;
 
      /* Bar layout */
      BARS_PER_LINE = 4;
      BAR_LINE_WIDTH = 10.0f;
      BAR_LINE_HEIGHT = 90.0f;
      
      /* Chord and text layout */
      CHORD_FONT_SIZE = 60;
      REPEAT_FONT_SIZE = 80;
      BAR_DESC_FONT_SIZE = 60;
      BAR_DESC_MARGIN_X = 30;
      BAR_DESC_MARGIN_Y = 70;
      TEXT_FONT_SIZE = 40;
      CHORD_MARGIN_X = 30.0f;
      CHORD_HEIGHT_ABOVE_BAR = 20.0f;
      TEXT_BELOW_BAR = 30.0f;    
      REPEAT_MARGIN_X = 30.0f;
      REPEAT_MARGIN_Y = 60.0f;
      
      BAR_SPACING_X = (PAGE_WIDTH - 2.0f*MARGIN_X)/BARS_PER_LINE;
      BAR_SPACING_Y = 3.0f * BAR_LINE_HEIGHT;
      
      x = MARGIN_X;
      y = MARGIN_TOP;
      out = outs;
      bars_this_line = 0;
      bar_desc_text = "";
  }
   
  public void printHeader() throws IOException {
      out.write("<svg width=\"210mm\" height=\"297mm\" viewBox=\"0 0 2100 2970\">\n" );
      out.flush();     	  	  
  }
  
  public void printFooter() throws IOException {
      out.write("</svg>\n" );
      out.flush();     	  	  
  }
  
  public void printTitle(String title) throws IOException {
	  float x1;
	  x1 = (PAGE_WIDTH/2.0f);

	  out.write
	      (    
		  "  <text x=\"" + Float.toString(x1) + "\" y=\""+ Float.toString(y) +"\"\n" +
          "    style=\"font-family: Arial;\n" +
          "           font-size  : " + Integer.toString(TITLE_FONT_SIZE) + ";\n" +
          "           stroke     : #000000;\n" +
          "           fill       : #000000;\n" +
		  "           text-anchor: middle;\n" +
          "          \">" + title + "</text>\n"
	  	  );
	  
	  y += BAR_LINE_HEIGHT*2;
      out.flush();
    //System.out.println("TITLE:"+ title + "\n" );
  }
  
  public void printBar(String pretext, java.util.Vector<BarInfo> chordtext
		              ,boolean bRepStart, boolean bRepEnd
		              ,boolean bDoubleBarStart, boolean bDoubleBarEnd)  throws IOException {
//      out.write("BAR:"+ pretext + "\n" );
	  float x1;
	  float y1;
	  float repeat_offset = 0;
	  if (bars_this_line == 0)
	  {
		  repeat_offset = 0;
		  printBarLine();
	  }
	  if (bDoubleBarStart) {
		  printDoubleBarLineStart();
	      repeat_offset = 5;
	  }
	  
	  if (bRepStart)
	  {
		  x1 = x + BAR_LINE_WIDTH + repeat_offset;
		  y1 = y + REPEAT_MARGIN_Y;
		  out.write
	      (    
		  "  <text x=\"" + Float.toString(x1) + "\" y=\""+ Float.toString(y1) +"\"\n" +
	      "    style=\"font-family: Serif;\n" +
	      "           font-size  : " + Integer.toString(REPEAT_FONT_SIZE) + ";\n" +
	      "           stroke     : #000000;\n" +
	      "           fill       : #000000;\n" +
		  "           text-anchor: left;\n" +
	      "          \">" + ":" + "</text>\n"
	  	  );	  
	  }

	  if (bar_desc_text.length()>0)
	  {
		  x1 = x + BAR_DESC_MARGIN_X + repeat_offset;
		  y1 = y + BAR_DESC_MARGIN_Y;
		  out.write
	      (    
		  "  <text x=\"" + Float.toString(x1) + "\" y=\""+ Float.toString(y1) +"\"\n" +
	      "    style=\"font-family: Serif;\n" +
	      "           font-size  : " + Integer.toString(BAR_DESC_FONT_SIZE) + ";\n" +
	      "           stroke     : #000000;\n" +
	      "           fill       : #000000;\n" +
		  "           text-anchor: left;\n" +
	      "          \">" + bar_desc_text + "</text>\n"
	  	  );	  	
		  bar_desc_text = "";
	  }
	  
	  x1 = x + CHORD_MARGIN_X;
	  float chord_offset = (BAR_SPACING_X - 2*CHORD_MARGIN_X)/chordtext.size();
      
	  /* Print chords */
	  y1 = y - CHORD_HEIGHT_ABOVE_BAR;
	  float y2 = y + TEXT_BELOW_BAR + BAR_LINE_HEIGHT;
	  for (BarInfo ct : chordtext) {
//            out.write("CHORD:" + ct.getChord()+"TEXT:" + ct.getText());
		  out.write
	      (    
		  "  <text x=\"" + Float.toString(x1) + "\" y=\""+ Float.toString(y1) +"\"\n" +
	      "    style=\"font-family: Serif;\n" +
	      "           font-size  : " + Integer.toString(CHORD_FONT_SIZE) + ";\n" +
	      "           stroke     : #000000;\n" +
	      "           fill       : #000000;\n" +
		  "           text-anchor: left;\n" +
	      "          \">" + ct.getChord() + "</text>\n"
	  	  );

		  out.write
	      (    
		  "  <text x=\"" + Float.toString(x1) + "\" y=\""+ Float.toString(y2) +"\"\n" +
	      "    style=\"font-family: Serif;\n" +
	      "           font-size  : " + Integer.toString(TEXT_FONT_SIZE) + ";\n" +
	      "           stroke     : #000000;\n" +
	      "           fill       : #000000;\n" +
		  "           text-anchor: left;\n" +
	      "          \">" + ct.getText() + "</text>\n"
	  	  );

		  x1 += chord_offset;
       }
      
	  x += BAR_SPACING_X;
	  if (bDoubleBarEnd)
	  {
		  repeat_offset = -10;
		  printDoubleBarLineEnd();
	  } else {
		  repeat_offset = 0;
		  printBarLine();
	  }
	  
	  if (bRepEnd)
	  {
		  x1 = x - BAR_LINE_WIDTH*3 + repeat_offset;
		  y1 = y + REPEAT_MARGIN_Y;
		  out.write
	      (    
		  "  <text x=\"" + Float.toString(x1) + "\" y=\""+ Float.toString(y1) +"\"\n" +
	      "    style=\"font-family: Serif;\n" +
	      "           font-size  : " + Integer.toString(REPEAT_FONT_SIZE) + ";\n" +
	      "           stroke     : #000000;\n" +
	      "           fill       : #000000;\n" +
		  "           text-anchor: right;\n" +
	      "          \">" + ":" + "</text>\n"
	  	  );	  
	  }

	  
	  bars_this_line++;
	  if (bars_this_line == BARS_PER_LINE)
	  {
		   x = MARGIN_X;
		   y = y + BAR_SPACING_Y;
		   bars_this_line = 0;
	  }
      out.flush();     	  
  }
  
  public void enterBarsBlock(String descText) throws IOException {
	  if (bars_this_line != 0)
	  {
		   x = MARGIN_X;
		   y = y + BAR_SPACING_Y;
		   bars_this_line = 0;		 
	  }
      bar_desc_text = descText;
  }
  
  /* Here are the low level rendering functions */
  public void printDoubleBarLineStart() throws IOException {
	  float x1,x2,y2;
	  
	  x1 = x + 15;
	  x2 = x1;
	  y2 = y + BAR_LINE_HEIGHT;
	  out.write
        (        		  
	    "  <line x1=\"" + Float.toString(x1) + "\" y1=\""+ Float.toString(y) +"\"" +
	         " x2=\"" + Float.toString(x2) + "\" y2=\"" + Float.toString(y2) +"\"" +
	         " style=\"stroke-width: 2; stroke: black;\"/>\n"
	    );
	  out.flush();
  }

  public void printDoubleBarLineEnd() throws IOException {
	  float x1,x2,y2;
	  
	  printBarLine();
	  x1 = x - 15;
	  x2 = x1;
	  y2 = y + BAR_LINE_HEIGHT;
	  out.write
        (        		  
	    "  <line x1=\"" + Float.toString(x1) + "\" y1=\""+ Float.toString(y) +"\"" +
	         " x2=\"" + Float.toString(x2) + "\" y2=\"" + Float.toString(y2) +"\"" +
	         " style=\"stroke-width: 2; stroke: black;\"/>\n"
	    );
	  out.flush();
  }

  public void printBarLine() throws IOException {
	  float x2,y2;
	  x2 = x;
	  y2 = y + BAR_LINE_HEIGHT;
	  out.write
        (        		  
	    "  <line x1=\"" + Float.toString(x) + "\" y1=\""+ Float.toString(y) +"\"" +
	         " x2=\"" + Float.toString(x2) + "\" y2=\"" + Float.toString(y2) +"\"" +
	         " style=\"stroke-width: 10; stroke: black;\"/>\n"
	    );
	  out.flush();
  }
  
  public void printTextBlock(String t) throws IOException {
    	
  }
  
  /* Still not sure what to do about repeats / double bar lines!
  /* Next How to split a string on a token? */
  /* myString.split("\\s+");*/
  
  /* Would save a lot of timeto put the svg into html? Just use revert on
   * inkscape */
  /* 
   * <!-- Multi-line text -->
<text x="25" y="360" fill="purple" font-size="18">
<tspan>Line 1</tspan>
<tspan x="35" dy="1em">Line 2</tspan>
<tspan x="45" dy="1em">Line 3</tspan>
</text>
*/
  
  
  /*
   * String      message = new String("Hello, StackOverflow!");
Font        defaultFont = new Font("Helvetica", Font.PLAIN, 12);
FontMetrics fontMetrics = new FontMetrics(defaultFont);
//...
int width = fontMetrics.stringWidth(message);
   */
}
