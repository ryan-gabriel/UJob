package Components;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class ModernButton extends JButton {
      private Color bgColor;
      private Color originalColor;
      
      public ModernButton(String text, Color backgroundColor, Color textColor, Color hoverColor) {
          super(text);
          this.bgColor = backgroundColor;
          this.originalColor = backgroundColor;
          
          setOpaque(false);
          setBorderPainted(false);
          setFocusPainted(false);
          setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
          setCursor(new Cursor(Cursor.HAND_CURSOR));
          setForeground(textColor);
          
          addMouseListener(new MouseAdapter() {
              @Override
              public void mouseEntered(MouseEvent e) {
                  ModernButton.this.bgColor = hoverColor;
                  repaint();
              }
              
              @Override
              public void mouseExited(MouseEvent e) {
                  ModernButton.this.bgColor = originalColor;
                  repaint();
              }
              
              @Override
              public void mousePressed(MouseEvent e) {
                  ModernButton.this.bgColor = hoverColor.darker();
                  repaint();
              }
              
              @Override
              public void mouseReleased(MouseEvent e) {
                  ModernButton.this.bgColor = hoverColor;
                  repaint();
              }
          });
      }
      
      @Override
      protected void paintComponent(Graphics g) {
          Graphics2D g2 = (Graphics2D) g.create();
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          
          // Fill background dengan corner radius kecil seperti gambar
          g2.setColor(bgColor);
          g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4); // Corner radius kecil
          
          // Draw text dengan posisi center
          g2.setColor(getForeground());
          g2.setFont(getFont());
          FontMetrics fm = g2.getFontMetrics();
          int x = (getWidth() - fm.stringWidth(getText())) / 2;
          int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
          g2.drawString(getText(), x, y);
          
          g2.dispose();
      }
  }