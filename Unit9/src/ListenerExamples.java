import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ListenerExamples {
    private static final ExecutorService EXEC = Executors.newCachedThreadPool();
    private final JButton colorButton = new JButton("Change color");
    private final Random random = new Random();

    private void backgroundRandom() {
        // 用于接收动作事件的监听器
        colorButton.addActionListener(new ActionListener() {
            // 发生动作时调用
            public void actionPerformed(ActionEvent e) {
                colorButton.setBackground(new Color(random.nextInt()));
            }
        });
    }

    private final JButton computeButton = new JButton("Big computation");

    private void longRunningTask() {
        // 用于接收动作事件的监听器
        computeButton.addActionListener(new ActionListener() {
            // 发生动作时调用
            public void actionPerformed(ActionEvent e) {
                EXEC.execute(new Runnable() {
                    public void run() {
                        /* 耗时运算 */
                    }
                });
            }
        });
    }


    private final JButton button = new JButton("Do");
    private final JLabel label = new JLabel("idle");

    /** 提供用户反馈的耗时任务 */
    private void longRunningTaskWithFeedback() {
        // 用于接收动作事件的监听器
        button.addActionListener(new ActionListener() {
            // 发生动作时调用
            public void actionPerformed(ActionEvent e) {
                button.setEnabled(false);
                label.setText("busy");
                EXEC.execute(new Runnable() {
                    public void run() {
                        try {
                            /* 耗时运算 */
                        } finally {
                            GuiExecutor.instance().execute(new Runnable() {
                                public void run() {
                                    button.setEnabled(true);
                                    label.setText("idle");
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private final JButton startButton = new JButton("Start");
    private final JButton cancelButton = new JButton("Cancel");
    private Future<?> runningTask = null;

    /** 取消耗时任务 */
    private void taskWithCancellation() {
        // 用于接收动作事件的监听器
        startButton.addActionListener(new ActionListener() {
            // 发生动作时调用
            public void actionPerformed(ActionEvent e) {
                if (runningTask != null) {
                    runningTask = EXEC.submit(new Runnable() {
                        public void run() {
                            while (moreWork()) {
                                if (Thread.currentThread().isInterrupted()) {
                                    cleanUpPartialWork();
                                    break;
                                }
                                doSomeWork();
                            }
                        }

                        private boolean moreWork() { return false; }
                        private void cleanUpPartialWork() { }
                        private void doSomeWork() { /* do something */ }
                    });
                }
                ;
            }
        });

        // 用于接收动作事件的监听器
        cancelButton.addActionListener(new ActionListener() {
            // 发生动作时调用
            public void actionPerformed(ActionEvent event) {
                if (runningTask != null) {
                    runningTask.cancel(true);
                }
            }
        });
    }

    /** 在 BackgroundTask 中启动一个耗时的、可取消的任务 */
    private void runInBackground(final Runnable task) {
        // 用于接收动作事件的监听器
        startButton.addActionListener(new ActionListener() {
            // 发生动作时调用
            public void actionPerformed(ActionEvent e) {
                class CancelListener implements ActionListener {
                    BackgroundTask<?> task;
                    public void actionPerformed(ActionEvent event) {
                        if (task != null) {
                            task.cancel(true);
                        }
                    }
                }
                final CancelListener listener = new CancelListener();
                listener.task = new BackgroundTask<Void>() {
                    public Void compute() {
                        while (moreWork() && !isCancelled()) {
                            doSomeWork();
                        }
                        return null;
                    }

                    private boolean moreWork() {
                        return false;
                    }

                    private void doSomeWork() {
                    }

                    public void onCompletion(boolean cancelled, String s, Throwable exception) {
                        cancelButton.removeActionListener(listener);
                        label.setText("done");
                    }
                };
                cancelButton.addActionListener(listener);
                EXEC.execute(task);
            }
        });
    }
}