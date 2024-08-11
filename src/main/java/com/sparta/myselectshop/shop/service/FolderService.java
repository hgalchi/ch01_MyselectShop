package com.sparta.myselectshop.shop.service;

import com.sparta.myselectshop.shop.dto.FolderResponseDto;
import com.sparta.myselectshop.shop.entity.Folder;
import com.sparta.myselectshop.auth.entity.User;
import com.sparta.myselectshop.shop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public void addFolders(List<String> folderNames, User user) {

    List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user,folderNames);

    List<Folder> folderList = new ArrayList<>();

    for (String folderName : folderNames) {
        if(!isExistFolderName(folderName,existFolderList)) {
            Folder folder = new Folder(folderName,user);
            folderList.add(folder);
        }else {
            throw new IllegalArgumentException("폴더명이 중복되었습니다.");
        }
    }

    folderRepository.saveAll(folderList);

    }


    public List<FolderResponseDto> getFolders(User user) {

        List<Folder> folderList = folderRepository.findAllByUser(user);
        List<FolderResponseDto> responseDtoList = new ArrayList<>();
        for(Folder folder : folderList) {
            responseDtoList.add(new FolderResponseDto(folder));
        }

        return responseDtoList;

    }


    private boolean isExistFolderName(String folderNames, List<Folder> existFolderList) {
        for (Folder existfolder : existFolderList) {
            if(folderNames.equals(existfolder.getName())) {
                return true;
            }

        }
        return false;
    }


}
