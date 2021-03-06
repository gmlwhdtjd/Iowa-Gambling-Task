import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = -2490080630765489433L;

    String programVersion = "1.0.1";

    Font font;
    Preferences prefs = Preferences.userNodeForPackage(MainFrame.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = MainFrame.getInstance();
            mainFrame.setContentPane(new StartLayer());
            mainFrame.setVisible(true);

            File dirForResult = new File("Results");
            if (!dirForResult.exists()) {
                if (!dirForResult.mkdirs())
                    System.err.println("디렉토리 생성 실패");
            }
        });
    }

    /**
     * 싱글톤 관련 메서드
     */
    private static class Singleton {
        private static final MainFrame instance = new MainFrame();
    }

    public static MainFrame getInstance() {
        return Singleton.instance;
    }

    /**
     * 메인 글래스 생성자
     */
    private MainFrame() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setTitle("Iowa Gambling Task - " + programVersion);
                setPreferredSize(new Dimension(460, 350));
                setForeground(Color.WHITE);
                setBackground(Color.WHITE);
                setResizable(false);

                try {
                    InputStream is = getClass().getResourceAsStream("/NanumGothic.ttc");
                    font = Font.createFont(Font.TRUETYPE_FONT, is);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                pack();
            }
        });
    }

    public void setLayer(JPanel newLayer) {
        SwingUtilities.invokeLater(() -> {
            setContentPane(newLayer);

            newLayer.setFocusable(true);
            newLayer.requestFocusInWindow();

            setVisible(true);
        });
    }

    public void setLoadingLayer() {
        SwingUtilities.invokeLater(() -> {
            JPanel nowLoadingLayer = new JPanel();
            nowLoadingLayer.setLayout(new BorderLayout());
            nowLoadingLayer.setBackground(Color.WHITE);

            ImageIcon loading = new ImageIcon(MainFrame.class.getResource("/Icon_Loading.gif"));
            JLabel nowLoadingLabel = new JLabel("Now loading", loading, JLabel.CENTER);
            nowLoadingLabel.setFont(font.deriveFont(Font.PLAIN, 15));
            nowLoadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nowLoadingLayer.add(nowLoadingLabel);

            setLayer(nowLoadingLayer);
        });
    }
}