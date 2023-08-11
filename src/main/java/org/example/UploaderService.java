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
                            .getOrElseThrow(this::handleUploadException);

        AddFileRequest addFileRequest = new AddFileRequest();
        addFileRequest.setStorageId(storageId);
        addFileRequest.setName(fileName);

        Try.of(() -> sourceFilesApi.addFile(projectId, addFileRequest))
           .getOrElseThrow(this::handleUploadException);

        log.info("File {} successfully uploaded!", fileName);
    }

    private CrowdinUploaderException handleUploadException(Throwable e) {
        if (e instanceof HttpException) {
            return new CrowdinUploaderException("Error: " + ((HttpException) e).getError().getMessage());
        } else if (e instanceof HttpBadRequestException) {
            return new CrowdinUploaderException("Error: File with this name already exists.");
        } else {
            return new CrowdinUploaderException("Error: " + e.getMessage());
        }
    }
}
