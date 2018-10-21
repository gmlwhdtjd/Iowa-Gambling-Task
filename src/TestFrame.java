import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestFrame extends JFrame {
    private static final long serialVersionUID = 6724272212132527965L;

    private MainFrame mainFrame = MainFrame.getInstance();

    private Decks decks;
    private AtomicBoolean isCardAction;
    private boolean isMainTest;

    //TestPanel Component
    private MoneyChartPanel moneyChartPanel;
    private MessageLabel messageLabel;
    private Card[] cards;

    //차트 관련 변수
    private String category = "금액";
    private String label[] = {"현금 보유액", "빌린 금액"};

    private DefaultCategoryDataset dataSet;

    //Sound
    private SoundClip soundClip;

    //Block 	관련변수
    private Long blockStartTime;
    private Long blockEndTime;

    private int currentBlockNumber;

    private String blockResult;
    private int blockCount[];

    // 저장용 string
    private Vector<String> outputStrings;

    // test values
    private String subjectNumber_;
    private String subjectName_;

    private int numberOfPracticeBlock;
    private int numberOfMainBlock;
    private int numberOfTrialOnEachBlock;

    private int currentTrialOnBlock;

    //Flow Thread
    private Thread practiceStartThread = new Thread(() -> {
        JPanel contentPanel = (JPanel) getContentPane();
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();

        dataSet.addValue(200000, category, label[0]);
        dataSet.addValue(200000, category, label[1]);

        SwingUtilities.invokeLater(() -> cardLayout.show(contentPanel, "PracticeStart"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //start practice test
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(contentPanel, "Test");
            currentBlockNumber = 0;
            startBlock();
        });
    });
    private Thread practiceEndThread = new Thread(new Runnable() {

        @Override
        public void run() {
            JPanel contentPanel = (JPanel) getContentPane();
            CardLayout cardLayout = (CardLayout) contentPanel.getLayout();

            //end practice test
            SwingUtilities.invokeLater(() -> cardLayout.show(contentPanel, "PracticeEnd"));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mainStartThread.start();
        }
    });
    private Thread mainStartThread = new Thread(() -> {
        JPanel contentPanel = (JPanel) getContentPane();
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();

        dataSet.setValue(200000, category, label[0]);
        dataSet.setValue(200000, category, label[1]);

        SwingUtilities.invokeLater(() -> cardLayout.show(contentPanel, "MainStart"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //start main test
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(contentPanel, "Test");
            currentBlockNumber = 0;
            startBlock();
        });
        isMainTest = true;
    });
    private Thread mainEndThread = new Thread(() -> {
        JPanel contentPanel = (JPanel) getContentPane();
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();

        //end main test
        SwingUtilities.invokeLater(() -> cardLayout.show(contentPanel, "MainEnd"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> cardLayout.show(contentPanel, "End"));
    });

    /**
     * Create the Frame. settingValues must contain 8 values.
     * values order = trial, interTrialInterval, studyTime, updatingSteps, CTI_Time, encodingTime, recallTime, typingTime .
     *
     * @throws Exception from settingValues
     */
    public TestFrame(String subjectNumber, String subjectName, int settingValues[]) throws Exception {
        setUndecorated(true);

        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        device.setFullScreenWindow(this);
        //setSize(new Dimension(720, 480));

        // 변수 설정
        isCardAction = new AtomicBoolean(false);
        isMainTest = false;
        decks = new Decks(40, 4);

        dataSet = new DefaultCategoryDataset();

        soundClip = new SoundClip("/Sound_getMoney.wav");

        subjectNumber_ = subjectNumber;
        subjectName_ = subjectName;

        if (settingValues.length == 3) {
            numberOfPracticeBlock = settingValues[0];
            numberOfMainBlock = settingValues[1];
            numberOfTrialOnEachBlock = settingValues[2];
        }
        else {
            throw new Exception("settingValues must contain 3 values");
        }

        Calendar cal = Calendar.getInstance(); //출력용으로 Calendar 클래스에서 Date 클래스를 가져옵니다.

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);

        String date = year + "-" + month + "-" + day;

        //output 설정
        outputStrings = new Vector<>();
        outputStrings.add("피실험자 번호," + subjectNumber_);
        outputStrings.add("피실험자 이름," + subjectName_);
        outputStrings.add("수행 날짜," + date);
        outputStrings.add("");
        outputStrings.add("설정값");
        outputStrings.add("연습 블록 수,메인 블록 수,블록당 시행 수");
        outputStrings.add("" + numberOfPracticeBlock + "," + numberOfMainBlock + "," + numberOfTrialOnEachBlock);
        outputStrings.add("");
        StringBuilder tmp = new StringBuilder();
        tmp.append("블록,");
        for (int i = 1; i <= numberOfTrialOnEachBlock; i++) {
            tmp.append("시행 ");
            tmp.append(i);
            tmp.append(",");
        }
        tmp.append("수행시간,net점수");
        outputStrings.add(tmp.toString());

        subjectNumber_ = subjectNumber_ + "_" + subjectName_ + "_" + date;

        //Panel 설정
        JPanel contentPanel = (JPanel) getContentPane();

        contentPanel.setLayout(new CardLayout(0, 0));
        contentPanel.add(getMessagePanelWithButton(
                "테스트를 시작하겠습니다",
                "Start",
                (ActionEvent e) -> practiceStartThread.start()),
                "Start");
        contentPanel.add(getMessagePanelWithButton(
                "모든 테스트가 종료되었습니다.",
                "결과를 저장하고 종료",
                (ActionEvent e) -> {
                    new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() {
                            // 파일 출력
                            try {
                                BufferedWriter out;

                                File resultsDir = new File("Results");
                                if (resultsDir.exists()) {
                                    out = new BufferedWriter(new OutputStreamWriter(
                                            new FileOutputStream("Results/" + subjectNumber_ + ".csv"), StandardCharsets.UTF_8));
                                }
                                else {
                                    out = new BufferedWriter(new OutputStreamWriter(
                                            new FileOutputStream("" + subjectNumber_ + ".csv"), StandardCharsets.UTF_8));
                                }

                                for (int i = 0; i < outputStrings.size(); i++) {
                                    if (i == 0)
                                        out.write("\uFEFF" + outputStrings.get(i));
                                    else
                                        out.write(outputStrings.get(i));
                                    out.newLine();
                                }

                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            SwingUtilities.invokeLater(() -> {
                                dispose();

                                JPanel newLayer = new StartLayer();

                                mainFrame.setLayer(newLayer);
                            });
                        }
                    }.execute();
                }),
                "End");

        contentPanel.add(getMessagePanel("연습 시행을 시작하겠습니다."), "PracticeStart");
        contentPanel.add(getMessagePanel("연습 시행이 종료되었습니다."), "PracticeEnd");

        contentPanel.add(getMessagePanel("본 시행을 시작하겠습니다."), "MainStart");
        contentPanel.add(getMessagePanel("본 시행이 종료되었습니다."), "MainEnd");

        contentPanel.add(getMainTestPanel(), "Test");

        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "Start");

        requestFocusInWindow();
    }

    private void doCardAction(Card currentCard) {
        if (!isCardAction.getAndSet(true)) {
            JButton currentButton = currentCard.getButton();
            blockEndTime = System.currentTimeMillis();

            new SwingWorker<Void, Void>() {
                DecimalFormat df = new DecimalFormat("###,###");

                @Override
                protected Void doInBackground() throws Exception {
                    soundClip.play();

                    SwingUtilities.invokeLater(() -> {
                        currentButton.setBorder(new LineBorder(Color.RED, 2, true));
                        for (Card card : cards) {
                            card.getButton().setEnabled(false);
                        }
                    });
                    int currentCardIndex = currentCard.getIndex();
                    int[] money = decks.getCard(currentCardIndex);

                    messageLabel.setPositiveStatus(" 당신은 " + df.format(money[0]) + "원을 얻었습니다.");

                    //차트 변경
                    int changes = dataSet.getValue(category, label[0]).intValue() + money[0];
                    dataSet.setValue(changes, category, label[0]);

                    //결과 저장
                    blockCount[currentCardIndex]++;
                    switch (currentCardIndex) {
                        case 0:
                            blockResult += "A,";
                            break;
                        case 1:
                            blockResult += "B,";
                            break;
                        case 2:
                            blockResult += "C,";
                            break;
                        case 3:
                            blockResult += "D,";
                            break;
                        default:
                            break;
                    }

                    Thread.sleep(1000);

                    if (money[1] > 0) {
                        messageLabel.setNegativeStatus(" 그러나 당신은 " + df.format(money[1]) + "원을 잃었습니다.");
                        // 차트 변경
                        if (changes < money[1]) {
                            dataSet.setValue(dataSet.getValue(category, label[1]).intValue() + money[1] - changes, category, label[1]);
                            changes = 0;
                        }
                        else {
                            changes = dataSet.getValue(category, label[0]).intValue() - money[1];
                        }
                        dataSet.setValue(changes, category, label[0]);

                        Thread.sleep(1000);
                    }

                    SwingUtilities.invokeLater(() -> {
                        currentButton.setBorder(new LineBorder(Color.gray, 2, true));
                        messageLabel.setDefualtStatus("잠시 기다려 주세요.");
                    });

                    Thread.sleep(500);
                    return null;
                }

                @Override
                protected void done() {
                    SwingUtilities.invokeLater(() -> {
                        for (Card card : cards) {
                            card.getButton().setEnabled(true);
                        }
                        cheakStat();
                        messageLabel.setDefualtStatus("카드 한장을 선택해 주세요.");

                        isCardAction.set(false);
                    });
                }
            }.execute();
        }
    }

    private void startBlock() {
        blockStartTime = System.currentTimeMillis();
        currentTrialOnBlock = 0;
        blockCount = new int[4];
        for (int i = 0; i < blockCount.length; i++)
            blockCount[i] = 0;
        if (isMainTest)
            blockResult = "메인 " + (currentBlockNumber + 1) + ",";
        else
            blockResult = "연습 " + (currentBlockNumber + 1) + ",";
    }

    private void endBlock() {
        double blockTime = ((double) blockEndTime - (double) blockStartTime) / 1000;
        blockResult += blockTime + ",";
        blockResult += (blockCount[2] + blockCount[3]) - (blockCount[0] + blockCount[1]);
        outputStrings.add(blockResult);
    }

    private void cheakStat() {
        currentTrialOnBlock++;
        if (currentTrialOnBlock == numberOfTrialOnEachBlock) {
            currentBlockNumber++;
            endBlock();

            if (isMainTest) {
                if (currentBlockNumber == numberOfMainBlock) // main test 종료
                    mainEndThread.start();
            }
            else {
                if (currentBlockNumber == numberOfPracticeBlock) // practice test 종료
                    practiceEndThread.start();
            }

            startBlock();
        }
    }

    private int getScreenDefaultSize() {
        int width = getSize().width * 7 / 10;
        int height = getSize().height;
        return (width < height ? width : height);
    }

    private JPanel getMainTestPanel() {
        JPanel mainTestPanel = new JPanel();
        mainTestPanel.setBackground(Color.WHITE);
        mainTestPanel.setLayout(new BoxLayout(mainTestPanel, BoxLayout.Y_AXIS));
        mainTestPanel.setFocusable(true);
        mainTestPanel.requestFocusInWindow();

        // 컨포넌트 생성
        Component verticalGlue_1 = Box.createVerticalGlue();
        Component verticalGlue_2 = Box.createVerticalGlue();
        Component verticalGlue_3 = Box.createVerticalGlue();
        Component verticalGlue_4 = Box.createVerticalGlue();

        Box horizontalBox_1 = Box.createHorizontalBox();
        Box horizontalBox_2 = Box.createHorizontalBox();
        Box horizontalBox_3 = Box.createHorizontalBox();

        Component horizontalGlue_1 = Box.createHorizontalGlue();
        Component horizontalGlue_2 = Box.createHorizontalGlue();
        Component horizontalGlue_3 = Box.createHorizontalGlue();
        Component horizontalGlue_4 = Box.createHorizontalGlue();
        Component horizontalGlue_5 = Box.createHorizontalGlue();

        messageLabel = new MessageLabel("카드 한장을 선택해 주세요.");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBackground(Color.WHITE);
        messageLabel.setFont(MainFrame.getInstance().font.deriveFont(Font.PLAIN, (int) (getScreenDefaultSize() / 20)));
        messageLabel.setPreferredSize(new Dimension(getPreferredSize().width, (int) (getScreenDefaultSize() / 10)));

        moneyChartPanel = new MoneyChartPanel(getScreenDefaultSize(), dataSet);

        cards = new Card[4];
        for (int i = 0; i < cards.length; i++) {

            cards[i] = new Card((char) (65 + i), i, getScreenDefaultSize());
            Card currentCard = cards[i];

            currentCard.getButton().setBorder(new LineBorder(Color.gray, 2, true));

            currentCard.getButton().addActionListener((ActionEvent e) -> {
                doCardAction(currentCard);

                setFocusable(true);
                requestFocusInWindow();
            });
        }

        // 컴포넌트 추가
        mainTestPanel.add(verticalGlue_1);

        mainTestPanel.add(horizontalBox_1);
        horizontalBox_1.add(moneyChartPanel);

        mainTestPanel.add(verticalGlue_2);

        mainTestPanel.add(horizontalBox_2);
        horizontalBox_2.add(messageLabel);

        mainTestPanel.add(verticalGlue_3);

        mainTestPanel.add(horizontalBox_3);

        horizontalBox_3.add(horizontalGlue_1);
        horizontalBox_3.add(cards[0]);
        horizontalBox_3.add(horizontalGlue_2);
        horizontalBox_3.add(cards[1]);
        horizontalBox_3.add(horizontalGlue_3);
        horizontalBox_3.add(cards[2]);
        horizontalBox_3.add(horizontalGlue_4);
        horizontalBox_3.add(cards[3]);
        horizontalBox_3.add(horizontalGlue_5);

        mainTestPanel.add(verticalGlue_4);

        return mainTestPanel;
    }

    private JPanel getMessagePanel(String str) {
        JPanel messagePanel = new JPanel();

        JLabel messageLabel = new JLabel(str);

        messagePanel.setLayout(new BorderLayout(0, 0));
        messagePanel.setBackground(Color.WHITE);

        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBackground(Color.WHITE);
        messageLabel.setFont(MainFrame.getInstance().font.deriveFont(Font.PLAIN, (int) (getScreenDefaultSize() / 15)));

        messagePanel.add(messageLabel);
        return messagePanel;
    }

    private JPanel getMessagePanelWithButton(String message, String btnString, ActionListener buttonAction) {
        JPanel startEndPanel = new JPanel();
        startEndPanel.setLayout(new BoxLayout(startEndPanel, BoxLayout.Y_AXIS));
        startEndPanel.setBackground(Color.WHITE);

        Component verticalGlue_1 = Box.createVerticalGlue();
        Component verticalGlue_2 = Box.createVerticalGlue();

        Component verticalStrut = Box.createVerticalStrut((int) (getScreenDefaultSize() / 20));

        JLabel label = new JLabel(message);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBackground(Color.WHITE);
        label.setFont(MainFrame.getInstance().font.deriveFont(Font.PLAIN, (int) (getScreenDefaultSize() / 15)));

        JButton button = new JButton(btnString);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(Color.WHITE);
        button.setFont(MainFrame.getInstance().font.deriveFont(Font.PLAIN, (int) (getScreenDefaultSize() / 20)));
        button.addActionListener(buttonAction);

        startEndPanel.add(verticalGlue_1);
        startEndPanel.add(label);
        startEndPanel.add(verticalStrut);
        startEndPanel.add(button);
        startEndPanel.add(verticalGlue_2);

        button.setFocusable(true);
        requestFocusInWindow();

        return startEndPanel;
    }
}

