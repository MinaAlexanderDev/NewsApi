package com.mina.news.breakingnews.repository

import com.mina.news.breakingnews.api.ServiceAPI
import com.mina.news.breakingnews.data.localdata.DatabaseArticle

interface Article : ServiceAPI, DatabaseArticle