package org.GUI.scoreboard;

import org.GUI.Gallery;
import org.controller.MatchController;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.JTable;
import java.awt.*;
import java.util.List;

public class Scoreboard extends JPanel
{
    int border = 15;
    JTable scoreboard;
    MatchController controller;
    Gallery gallery;
    String[] columnNames = {"Platz", "Spieler"};
    String[][] values;

    public Scoreboard(MatchController controller)
    {
        this.controller = controller;
        this.gallery = Gallery.galleryInstance();
        this.setLayout(new BorderLayout());
        update();
    }

    private void fillScoreboard()
    {
        List<Integer> winners = controller.getWinners();
        values = new String[winners.size()][columnNames.length];
        int rank = 1;

        DefaultTableModel model = new DefaultTableModel(columnNames, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex)
            {
                if (columnIndex == 1) {
                    return ImageIcon.class;
                }
                return String.class;
            }
        };

        for (int i : winners)
        {
            ImageIcon playerIcon = new ImageIcon(gallery.shipAtlas.get(i-1));
            model.addRow(new Object[]{String.valueOf(rank), playerIcon});
            rank ++;
        }
        this.scoreboard = new JTable(model);
    }

    public void update()
    {
        if(controller.getWinners().isEmpty())
        {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
        this.setOpaque(false);
        this.removeAll();
        this.fillScoreboard();
        this.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));

        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        scoreboard.getColumnModel().getColumn(0).setCellRenderer(defaultTableCellRenderer);
        scoreboard.setFont(gallery.getFont("scoreboardFont"));
        scoreboard.getTableHeader().setFont(gallery.getFont("scoreboardHeaderFont"));

        // Höhere Zeilen für Bilder
        this.scoreboard.setRowHeight(50);
        scoreboard.setOpaque(false);
        scoreboard.setBackground(new Color(255, 255, 255, 100));

        JScrollPane jsp = new JScrollPane(scoreboard);
        jsp.setOpaque(false);
        jsp.getViewport().setOpaque(false);
        jsp.getViewport().setBackground(new Color(255, 255, 255, 100));
        this.add(jsp, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}


