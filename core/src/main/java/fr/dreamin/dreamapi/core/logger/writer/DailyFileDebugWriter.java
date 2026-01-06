package fr.dreamin.dreamapi.core.logger.writer;

import fr.dreamin.dreamapi.api.logger.DebugWriter;
import fr.dreamin.dreamapi.api.logger.LogEntry;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Debug writer implementation that writes log entries to daily log files.
 *
 * @author Dreamin
 * @since 1.0.0
 */
public final class DailyFileDebugWriter implements DebugWriter {

  private static final DateTimeFormatter DATE_FORMAT =
    DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private static final DateTimeFormatter TS_FORMAT =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final File folder;

  private LocalDate currentDate;
  private BufferedWriter writer;

  private volatile boolean enabled = true;

  // ##############################################################
  // ---------------------- SERVICE METHODS -----------------------
  // ##############################################################

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public void write(@NotNull LogEntry entry) throws Exception {
    final var today = entry.timestamp().toLocalDate();

    if (this.writer == null || !today.equals(this.currentDate))
      rotate(today);

    final var ts = entry.timestamp().format(TS_FORMAT);
    final var line = String.format(
      "%s [DreamAPI][%s][%s][%s::%s:%d] - %s",
      ts,
      entry.level().name(),
      entry.threadName(),
      entry.category(),
      entry.context().methodName(),
      entry.context().lineNumber(),
      entry.message()
    );

    this.writer.write(line);
    this.writer.newLine();
    this.writer.flush();

  }

  // ###############################################################
  // ----------------------- PUBLIC METHODS ------------------------
  // ###############################################################

  /**
   * Creates a new DailyFileDebugWriter that writes log entries to the specified folder.
   *
   * @param folder the folder where log files will be stored
   *
   * @author Dreamin
   * @since 1.0.0
   */
  public DailyFileDebugWriter(final @NotNull File folder) {
    this.folder = folder;
    if (!this.folder.exists() && !this.folder.mkdirs())
      throw new IllegalStateException(String.format("Unable to create debug folder: %s", this.folder.getAbsolutePath()));
  }

  /**
   * Closes the writer and releases any associated resources.
   * This method should be called during plugin shutdown to ensure proper resource cleanup.
   *
   * @author Dreamin
   * @since 1.0.0
   */
  public synchronized void close() {
    try {
      if (this.writer != null) {
        this.writer.flush();
        this.writer.close();
      }
    } catch (Exception ignored) {}
  }

  // ###############################################################
  // ----------------------- PRIVATE METHODS -----------------------
  // ###############################################################

  /**
   * Rotates the log file to a new file for the specified date.
   *
   * @param newDate the new date for the log file
   * @throws Exception if an I/O error occurs
   *
   * @author Dreamin
   * @since 1.0.0
   */
  private void rotate(final @NotNull LocalDate newDate) throws Exception {
    if (this.writer != null) {
      this.writer.flush();
      this.writer.close();
    }

    if (!this.folder.exists() && !this.folder.mkdirs()) {
      throw new IllegalStateException(
        String.format("Unable to create debug folder: %s", this.folder.getAbsolutePath())
      );
    }

    this.currentDate = newDate;
    final var fileName = DATE_FORMAT.format(newDate) + ".log";
    final var file = new File(this.folder, fileName);

    this.writer = new BufferedWriter(new FileWriter(file, true));
  }

}
