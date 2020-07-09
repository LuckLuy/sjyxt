package com.ly.sjyxt.controller;


import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.font.NumericShaper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通过读取word文件，分.doc和.docx获取文本内容
 * 根据传入的参数章节名称（可以多个）、关键字（可以多个）
 *
 * @author hyf
 * @create 2019-10-21 18:01
 **/
@RestController
public class ReadWordController {
  /**
   * 根据传入的标题名称（可以是多个），关键字（可以是多个）去查询word文档在对应的标题名称里是否含有这些关键字
   * @param title  以逗号的格式分隔，标题名称-例如1,2,3
   * @param keyword  关键字，一逗号格式分隔，
   * @param filePath  word文档所在的位置，
   * @return
   * @throws IOException
   */
  @RequestMapping(value="/checkContextByTitle_new/{title}/{keyword}/{filePath}",method= RequestMethod.GET)

 /*
 public boolean checkContextByTitle(@PathVariable("title") String title, @PathVariable("keyword") String keyword, @PathVariable("filePath") String filePath) throws IOException {
    //初始化返回值
    Boolean result = false;
    //word文件地址
    filePath = "C:\\Users\\HYF\\Desktop\\新建文件夹\\"+filePath;
    //title，处理问题数据，将中文逗号可以替换成英文逗号，为以后数据做准备
    title = title.replace("，",",");
    //处理关键字，原因同上
    keyword = keyword.replace("，",",");
    //拆分标题名称
    List<String> titleList = null;
    //拆分多个关键字
    List<String> keywordList = null;
    if(title != null && title.length() != 0) {
      titleList = Arrays.asList(title.split(","));
    }
    if(keyword != null && keyword.length() != 0) {
      keywordList = Arrays.asList(keyword.split(","));
    }
    //判断处理是doc文件还是docx文件
    if (filePath.endsWith(".doc")) {
      //为2003版doc
      //创建HWPFDocument
      HWPFDocument doc = creatHWPFDocument(filePath);
      //根据标题，关键字处理数据看标题中是否含有关键字，返回true和false
      result = getWord2003Context(doc,titleList,keywordList);
    }else if(filePath.endsWith("docx")){
      //为2007以上版docx
      XWPFDocument docx = creatXWPFDocument(filePath);
      //根据标题，关键字处理数据看标题中是否含有关键字，返回true和false
      result = getWord2007Context(docx,titleList,keywordList);
    }else{
      System.out.println("此文件不是word文件！");
    }
    return result;
  }
*/
  /**
   * 根据标题，找到相对应的段落文件，然后根据关键，判断是否含有这些关键字，2007以上
   * @param docx
   * @param titleList
   * @param keywordList
   * @return
   */
  /*public boolean getWord2007Context(XWPFDocument docx ,List<String> titleList,List<String> keywordList){
    //返回结果，初始化为false
    boolean result = false;
    //获取关键字返回结果，
    boolean checkKeywordresult = false;
    //获取所有段落
    List<XWPFParagraph> paras = docx.getParagraphs();
    //所有一级标题所在的位置
    List lvl0List = new ArrayList();
    //所有一标题的内容list
    List titleLel0List = new ArrayList();
    lvl0List = getListByLvl2007(docx,"0",0);
    titleLel0List = getListByLvl2007(docx,"0",1);
    int StartNum = 0;
    int endNum = 0;
    for(int i=0;i<titleList.size();i++){
      //获取传入标题；
      String title = titleList.get(i).trim();
      //根据每一个标题，开始段落和结束段落
      for (int a=0;a<titleLel0List.size();a++){
        //如果一级标题里包含传入标题，则获取开始段落和结束段落
        if(titleLel0List.get(a).toString().contains(title)){
          StartNum = (Integer) lvl0List.get(a);
          endNum = (Integer) lvl0List.get(a+1);
          //对比关键字，根据获取段落内容
          checkKeywordresult =  checkKeyword2007( paras, StartNum, endNum, keywordList);
          result = result||checkKeywordresult;//只要有一个存在则返回的就是true||
          break;
        }
      }
    }
    return result;
  }
*/
  /**
   * 根据关键字，查询对应的段落是否含有，
   * @param paras
   * @param StartNum
   * @param endNum
   * @param keyword
   * @return 有返回true，反之false
   */
  public boolean checkKeyword2007(List<XWPFParagraph> paras,int StartNum,int endNum,List keyword){
    String  content = "";
    boolean result = false;
    for(int i=StartNum;i<endNum;i++){
      content = content+paras.get(i).getParagraphText().trim();
    }
    for (int a = 0 ; a < keyword.size() ; a++ ){
      //循环每个keyword是否包含在内容里
      if(content.contains(keyword.get(a).toString().trim())){
        result = true;
        break;
      }
    }
    return result;
  }
  /**
   *
   * @param docx
   * @param titleLvlParam 传入级别
   * @param type 返回数据类型0为段落数list，1为标题list
   * @return
   */
 /* public List getListByLvl2007(XWPFDocument docx,String titleLvlParam,int type){
    List paragraphNumList = new ArrayList();
    List paragraphTitleList = new ArrayList();
    List<XWPFParagraph> paras = docx.getParagraphs();
    for (int a=0;a<paras.size();a++) {
      String titleLvl = getTitleLvl2007(docx,paras.get(a));//获取段落级别
      if(titleLvl.equals(titleLvlParam)){
        paragraphNumList.add(a);
        paragraphTitleList.add(paras.get(a).getParagraphText().trim());
      }
    }
    if(type==0){
      return paragraphNumList;
    }else if (type==1){
      return paragraphTitleList;
    }
    return null;
  }
*/
  /**
   * 获取段落级别
   * @param docx
   * @param para
   * @return
   */
  /*private static String getTitleLvl2007(XWPFDocument docx, XWPFParagraph para) {
    String titleLvl = "";
    //判断该段落是否设置了大纲级别
    try {
      if (para.getCTP().getPPr().getOutlineLvl() != null){
        return String.valueOf(para.getCTP().getPPr().getOutlineLvl().getVal());
      }
    } catch (Exception e) {

    }
    try {
      //判断该段落的样式是否设置了大纲级别
      if (docx.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl() != null) {
        return String.valueOf(docx.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal());
      }
    } catch (Exception e) {

    }
    try {
      //判断该段落的样式的基础样式是否设置了大纲级别
      if (docx.getStyles().getStyle(docx.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal())
          .getCTStyle().getPPr().getOutlineLvl() != null) {
        String styleName = docx.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal();
        return String.valueOf(docx.getStyles().getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal());
      }
    } catch (Exception e) {

    }

    try {
      if(para.getStyleID()!=null){
        return para.getStyleID();
      }
    } catch (Exception e) {

    }
    return titleLvl;
  }
*/

  /**
   * 根据标题，找到相对应的段落文件，然后根据关键，判断是否含有这些关键字，
   * @param doc
   * @param titleList
   * @param keywordList
   * @return  如果含有这些关键字，则返回true，反之返回false
   */
  /*public boolean getWord2003Context(HWPFDocument doc,List<String> titleList,List<String> keywordList){
    //返回结果，初始化为false
    boolean result = false;
    //获取关键字返回结果，
    boolean checkKeywordresult = false;
    //因为传入的标题，我们只对比 一级标题，所以我们只取段落级别为0的数据，段落级别分0-8,9为正文
    Range range = doc.getRange();//获取段落文本
    Paragraph paragraph = null;//每一段落
    //所有一级标题所在的位置
    List lvl0List = new ArrayList();
    //所有一标题的内容list
    List titleLel0List = new ArrayList();
    lvl0List = getListByLvl2003(0,range,0);
    titleLel0List = getListByLvl2003(0,range,1);
    int StartNum = 0;
    int endNum = 0;
    for(int i=0;i<titleList.size();i++){
      //获取传入标题；
      String title = titleList.get(i).trim();
      //根据每一个标题，开始段落和结束段落
      for (int a=0;a<titleLel0List.size();a++){
        //如果一级标题里包含传入标题，则获取开始段落和结束段落
        if(titleLel0List.get(a).toString().contains(title)){
          StartNum = (Integer) lvl0List.get(a);
          endNum = (Integer) lvl0List.get(a+1);
          //对比关键字，根据获取段落内容
          checkKeywordresult =  checkKeyword2003( range, StartNum, endNum, keywordList);
          result = result||checkKeywordresult;//只要有一个存在则返回的就是true||
          break;
        }
      }
    }
    return  result;
  }
*/
  /**
   *
   * @param range 段落文本
   * @param StartNum 开始段落
   * @param endNum   结束段落
   * @param keyword   关键字，多个
   * @return
   */
  public boolean checkKeyword2003(Range range,int StartNum,int endNum,List keyword){
    String  content = "";//为所有的段落文本
    boolean result = false;
    for(int i=StartNum;i<endNum;i++){
      content = content+range.getParagraph(i).text().trim();
    }
    for (int a = 0 ; a < keyword.size() ; a++ ){
      //循环每个keyword是否包含在内容里
      if(content.contains(keyword.get(a).toString().trim())){
        result = true;
        break;
      }
    }
    return result;
  }
  /**
   *
   * @param titleLvlParam 比对级别
   * @param range  段落文本
   * @param type   返回数据类型0为段落数list，1为标题list
   * @return
   */
  public List getListByLvl2003(int titleLvlParam,Range range,int type){
    List paragraphNumList = new ArrayList();
    List paragraphTitleList = new ArrayList();
    for(int i=0;i<range.numParagraphs();i++){
      //判断当前段落级别和传入级别是否相等，相等将段落数，放在list中；
      if(range.getParagraph(i).getLvl()==titleLvlParam){
        paragraphNumList.add(i);
        paragraphTitleList.add(range.getParagraph(i).text().trim());
      }
    }
    if(type==0){
      return paragraphNumList;
    }else if (type==1){
      return paragraphTitleList;
    }
    return null;
  }



  /**
   * 获取HWPFDocument对象，应用于2003版.doc
   * @param file
   * @return
   * @throws IOException
   */
  public HWPFDocument creatHWPFDocument(String file) throws IOException {
    InputStream is = new FileInputStream(file);
    HWPFDocument document = new HWPFDocument(is);
    return document;
  }

  /**
   * 获取XWPFDocument对象，应用于2007版.docx
   * @param file
   * @return
   * @throws IOException
   */
 /* public XWPFDocument creatXWPFDocument(String file) throws IOException {
    InputStream is = new FileInputStream(file);
    XWPFDocument document = new XWPFDocument(is);
    return document;
  }
*/
}

