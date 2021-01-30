package com.singh.getting.api;

import com.singh.getting.sftp.SFTPFile;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;

@Path("/service")
public class SFTPResource extends SFTPFile {

    @POST
    @Path("/downloadFile")
    @Produces(MediaType.TEXT_PLAIN)
    public String downloadFile() {
        String filePath = "/home/singh/file/";
        String fileName = "sftp.txt";

        ByteArrayOutputStream output = download(filePath, fileName);
        if (output.toByteArray().length > 0 && output.toByteArray() != null) {
            return "download completed and found data in file";
        } else {
            return "download completed but not found data in file";
        }
    }

    @POST
    @Path("/updateFile")
    @Produces(MediaType.TEXT_PLAIN)
    public String uploadFile() {
        String filePath = "/home/singh/file/";
        String fileName = "sftp.txt";
        String FileNameForUpload = "sftp2.txt";

        ByteArrayOutputStream output = download(filePath, fileName);
        if (output.toByteArray().length > 0 && output.toByteArray() != null) {
            upload(FileNameForUpload, output);
        }

        return "upload a file completed";
    }
}