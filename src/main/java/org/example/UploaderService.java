package org.example;

import com.crowdin.client.Client;
import com.crowdin.client.core.model.Credentials;
import com.crowdin.client.sourcefiles.SourceFilesApi;
import com.crowdin.client.sourcefiles.model.AddFileRequest;
import com.crowdin.client.storage.StorageApi;
import io.vavr.control.Try;
import org.example.utils.FileMatcher;

import java.io.FileInputStream;
import java.util.Arrays;

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
        Arrays.stream(filesToUpload)
              .forEach(this::uploadFile);
    }

    private void uploadFile(String fileName) {
        var storageResponseObject =
                Try.of(() -> storageApi.addStorage(fileName, new FileInputStream(fileName)))
                   .onFailure(Throwable::printStackTrace)
                   .get();
        Long id = storageResponseObject.getData()
                                       .getId();

        AddFileRequest addFileRequest = new AddFileRequest();
        addFileRequest.setStorageId(id);
        addFileRequest.setName(fileName);
        sourceFilesApi.addFile(projectId, addFileRequest);
    }
}
