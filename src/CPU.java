import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class CPU {
    public static List<CPU> all_cpus;
    public int hz;
    public List<IntConsumer> functions_list;
    Thread thread;
    private boolean isPlay;
    private long elapsedMilli;
    private boolean isPlayedBeforeStop;

    public CPU(final int hz, final String name) {
        this.functions_list = new ArrayList<>();
        this.isPlay = false;
        this.isPlayedBeforeStop = false;

        this.hz = hz;
        this.elapsedMilli = 0;
        this.thread = new Thread("Eventor_" + name) {
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (final InterruptedException e) {
                }
                CPU.this.thread_run();
            }
        };
        thread.start();

        if (CPU.all_cpus == null) {
            CPU.all_cpus = new ArrayList<>();
        }

        CPU.all_cpus.add(this);
    }

    public static void stopAllCPUS() {
        for (int i = 0; i < CPU.all_cpus.size(); i++) {
            CPU.all_cpus.get(i).isPlay = false;
        }
    }

    public static void resumeAllCPUS() {
        for (int i = 0; i < CPU.all_cpus.size(); i++) {
            CPU.all_cpus.get(i).resume();
        }
    }

    synchronized void resume() {
        if (this.isPlayedBeforeStop) {
            this.isPlay = true;
            this.notify();
        }
    }


    public void addFunction(final IntConsumer a) {
        this.functions_list.add(a);
    }

    public void play() {
        this.isPlay = true;
        this.isPlayedBeforeStop = true;
        this.resume();
    }

    public void stop() {
        this.isPlay = false;
        this.isPlayedBeforeStop = false;
    }

    public void thread_run() {
        int functions_size = 0;
        int[] last_sample_times = null;
        long last_sample;
        int i = 0;

        int time_to_sleep = 2;
        if (1000 / this.hz > 1) {
            time_to_sleep = 1000 / this.hz;
        }

        while (true) {

            if (functions_size != this.functions_list.size()) {
                functions_size = this.functions_list.size();
                last_sample_times = new int[functions_size];
                i = 0;
            }
            if (functions_size == 0) {
                continue;
            }

            last_sample = System.currentTimeMillis();

            try {
                Thread.sleep(time_to_sleep);
                synchronized (this) {
                    while (!isPlay) {
                        this.wait();
                        last_sample = System.currentTimeMillis();
                    }
                }
            } catch (final InterruptedException e) {
            }

            final int diff = (int) (System.currentTimeMillis() - last_sample);
            final int before_index = this.getCyclic(i - 1, functions_size);
            final int actual_diff = last_sample_times[before_index] + diff - last_sample_times[i];
            last_sample_times[i] = last_sample_times[before_index] + diff;

            final IntConsumer curr_func = this.functions_list.get(i);
            curr_func.accept(actual_diff);
            this.elapsedMilli += actual_diff;
            i++;
            i %= functions_size;
        }
    }

    private int getCyclic(int i, final int size) {
        i %= size;
        if (i < 0) {
            return size + i;
        }
        return i;
    }


}
