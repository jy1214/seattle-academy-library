<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="UTF-8">
<title>書籍の編集｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="resources/js/thumbnail.js"></script>
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />       
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
        <form action="<%=request.getContextPath()%>/updateBook" method="post" enctype="multipart/form-data" id="data_upload_form">
            <h1>書籍の編集</h1>
            <div class="content_body add_book_content">
                <div>
                    <span>書籍の画像</span> <span class="care care1">任意</span>
                    <div class="book_thumnail">
                        <a href="${bookInfo.thumbnailUrl}" data-lightbox="image-1"> <c:if test="${bookInfo.thumbnailUrl == 'null'}">
                                <img class="book_noimg" src="resources/img/noImg.png">
                            </c:if> <c:if test="${bookInfo.thumbnailUrl != 'null'}">
                                <img class="book_noimg" src="${bookInfo.thumbnailUrl}">
                            </c:if> <input type="hidden" name="bookId" value="${bookInfo.bookId}">
                        </a>
                    </div>
                    <input type="file" accept="image/*" name="thumbnail" id="thumbnail">
                </div>
                <div class="content_right">
                    <div>
                        <c:if test="${!empty errorText}">
                            <div class="error">${errorText}</div>
                        </c:if>
                        <span>書籍名</span><span class="care care2">必須</span>
                            <input type="text" name="title" value="${bookInfo.title}">
                    </div>
                    <div>
                        <span>著者名</span><span class="care care2">必須</span>
                            <input type="text" name="author" value="${bookInfo.author}">
                    </div>
                    <div>
                        <span>出版社</span><span class="care care2">必須</span>
                            <input type="text" name="publisher" value="${bookInfo.publisher}">
                    </div>
                    <div>
                        <span>出版日</span><span class="care care2">必須</span> <input type="text" name="publish_date" value="${bookInfo.publishDate}" placeholder="YYYYMMDD">
                    </div>
                    <div>
                        <span>ISBN</span><span class="care care1">任意</span> <input type="text" name="ISBN" value="${bookInfo.ISBN}">
                    </div>
                    <div>
                        <span>説明文</span><span class="care care1">任意</span> <input type="text" name="introduce" value="${bookInfo.introduce}">
                    </div>
                    <input type="hidden" id="bookId" name="bookId" value="${bookInfo.bookId}">
                </div>
            </div>
            <div class="addBookBtn_box">
                <button type="submit" id="return-btn" class="btn_returnBook">更新</button>
            </div>
        </form>
    </main>
</body>
</html>