import javax.swing.*;
import java.awt.*;

public class Card extends JPanel {
    private static final long serialVersionUID = 7733688628892916484L;

    private JButton button;
    private int index_;

    /**
     * Create the panel.
     */
    public Card(char text, int index, int screeDefaultSize) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.WHITE);

        index_ = index;

        //버튼 생성
        ImageIcon cardImage = new ImageIcon(getClass().getResource("/Icon_Card.jpg"));
        Image imgTmp = cardImage.getImage();
        imgTmp = imgTmp.getScaledInstance((int) (screeDefaultSize / 3.3),
                (int) (screeDefaultSize / 2.2),
                java.awt.Image.SCALE_SMOOTH);
        cardImage = new ImageIcon(imgTmp);

        Box verticalBox = Box.createVerticalBox();

        button = new JButton(cardImage);
        button.setDisabledIcon(cardImage);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        //버튼 생성 끝

        JLabel cardLabel = new JLabel(String.valueOf(text));
        cardLabel.setFont(MainFrame.getInstance().font.deriveFont(Font.PLAIN, (int) (screeDefaultSize / 20)));
        cardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(verticalBox);

        verticalBox.add(button);
        verticalBox.add(cardLabel);
    }

    public JButton getButton() {
        return button;
    }

    public int getIndex() {
        return index_;
    }
}
