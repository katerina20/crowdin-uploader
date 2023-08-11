package org.example;

import com.crowdin.client.Client;
import com.crowdin.client.core.http.exceptions.HttpBadRequestException;
import com.crowdin.client.core.http.exceptions.HttpException;
import com.crowdin.client.core.model.Credentials;
import com.crowdin.client.sourcefiles.SourceFilesApi;
import com.crowdin.client.sourcefiles.model.AddFileRequest;
import com.crowdin.client.storage.StorageApi;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.example.utils.FileMatcher;

import java.io.FileInputStream;
import java.util.Arrays;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Log4j2
public class UploaderService {
    private final Long projectId;
    private final StorageApi storageApi;
    private final SourceFilesApi sourceFilesApi;

    public UploaderService(String token, Long projectId) {
        this.projectId = projectId;
        Credentials credentials = new Credentials(token, null);
        Client client = new Client(credentials);
        storageApi = client.getStorageApi();
        sourceFilesApi = client.getSourceFilesApi();
    }

    public void upload(String wildcard) {
        String[] filesToUpload = FileMatcher.match(wildcard);
        log.info("Found {} files matching the wildcard.", filesToUpload.length);
        Arrays.stream(filesToUpload)
              .forEach(this::uploadFile);
    }

    private void uploadFile(String fileName) {
        log.info("Uploading {}...", fileName);
        Long storageId = Try.of(() -> storageApi.addStorage(fileName, new FileInputStream(fileName)))
                            .map(response -> response.getData().getId())
                            .onFailure(this::handleUploadException)
                            .get();

        if (isNull(storageId)) throw new CrowdinUploaderException("Error: Failed to obtain storage ID.");
        AddFileRequest addFileRequest = new AddFileRequest();
        addFileRequest.setStorageId(storageId);
        addFileRequest.setName(fileName);

        var response = Try.of(() -> sourceFilesApi.addFile(projectId, addFileRequest))
                          .onFailure(this::handleUploadException).getOrNull();
        if (nonNull(response) && "active".equals(response.getData().getStatus()))
            log.info("File {} successfully uploaded!", fileName);
        else log.error("File {} was not uploaded.", fileName);
    }

    private void handleUploadException(Throwable e) {
        if (e instanceof HttpBadRequestException) {
            log.error("Error: File with this name already exists.");
        } else if (e instanceof HttpException) {
            throw new CrowdinUploaderException("Error: " + ((HttpException) e).getError().getMessage());
        } else {
            throw new CrowdinUploaderException("Error: " + e.getMessage());
        }
    }
}
