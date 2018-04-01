package com.rongsheng.voiceimagerecognition;

import java.util.List;

/**
 * @author : lei
 * @desc :
 * @date : 2018/3/27 0027  上午 8:51.
 * 个人博客站: http://www.bestlei.top
 */

public class ImageBean {
    private List<WordsResult> words_result;
    public List<WordsResult> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResult> words_result) {
        this.words_result = words_result;
    }
    public class WordsResult {

        private String words;


        public void setWords(String words) {
            this.words = words;
        }

        public String getWords() {
            return words;
        }
    }
}