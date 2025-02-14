package ru.feryafox.javaschooltask17;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import ru.feryafox.javaschooltask17.service.DownloadOrchestrator;
import ru.feryafox.javaschooltask17.service.LinkDownloader;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DownloadOrchestratorTest {

    @Mock
    private LinkDownloader linkDownloader;

    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private DownloadOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        Mockito.clearInvocations(linkDownloader, executorService);
    }

    @Test
    void testStartDownloads() {
        List<String> links = List.of("https://www.sample-videos.com/img/Sample-png-image-1mb.png", "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf");
        Path downloadDir = Path.of("testDownloads");

        orchestrator.startDownloads(links, downloadDir);

        verify(executorService, times(2)).submit(any(Runnable.class));
    }

    @ParameterizedTest
    @MethodSource("fileNameProvider")
    void testExtractFileName(String link, String expectedFileName) throws Exception {
        Method method = DownloadOrchestrator.class
                .getDeclaredMethod("extractFileName", String.class);
        method.setAccessible(true);

        String actualFileName = (String) method.invoke(orchestrator, link);

        Assertions.assertEquals(expectedFileName, actualFileName);
    }

    static Stream<Arguments> fileNameProvider() {
        return Stream.of(
                Arguments.of("https://speed.hetzner.de/100MB.bin", "100MB.bin"),
                Arguments.of("https://github.com/git/git/archive/refs/heads/master.zip", "master.zip"),
                Arguments.of("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf", "dummy.pdf"),
                Arguments.of("https://file-examples.com/storage/fe7a97ebd451ad3fd3cbb3e/2017/04/file_example_MP4_1280_10MG.mp4", "file_example_MP4_1280_10MG.mp4"),
                Arguments.of("https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png", "PNG_transparency_demonstration_1.png"),
                Arguments.of("https://sample-videos.com/audio/mp3/wave.mp3", "wave.mp3"),
                Arguments.of("https://people.sc.fsu.edu/~jburkardt/data/csv/hw_200.csv", "hw_200.csv"),

                Arguments.of("https://github.com/", "unknown_file"),
                Arguments.of("https://www.wikipedia.org/", "unknown_file"),
                Arguments.of("https://sourceforge.net/projects/sevenzip/", "unknown_file")
        );
    }
}
