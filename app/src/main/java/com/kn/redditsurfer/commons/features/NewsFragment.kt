package com.kn.redditsurfer.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kn.redditsurfer.R
import com.kn.redditsurfer.commons.InfiniteScrollListener
import com.kn.redditsurfer.commons.RedditNews
import com.kn.redditsurfer.commons.RxBaseFragment
import com.kn.redditsurfer.commons.adapter.NewsAdapter
import com.kn.redditsurfer.commons.commons.old.inflate
import com.kn.redditsurfer.commons.features.NewsManager
import kotlinx.android.synthetic.main.news_fragment.news_list
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class NewsFragment : RxBaseFragment() {

    private var redditNews: RedditNews? = null
    private val newsManager by lazy {   //this makes it lazy-loaded
        NewsManager()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.news_fragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        news_list.setHasFixedSize(true)
        val linearLayout = LinearLayoutManager(context)
        news_list.layoutManager = linearLayout
        news_list.clearOnScrollListeners()
        news_list.addOnScrollListener(InfiniteScrollListener({ requestNews() }, linearLayout))
        initAdapter()

        if (savedInstanceState == null) {
            requestNews()
        }
    }

    private fun requestNews() {
        //(news_list.adapter as NewsAdapter).addNews(news)
        /**
         * first time will send empty string for after parameter.
         * Next time we will have redditNews set with the next page to
         * navigate with the after param.
         */
        val subscription = newsManager.getNews(redditNews?.after ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { retrievedNews ->
                    redditNews = retrievedNews
                    (news_list.adapter as NewsAdapter).addNews(retrievedNews.news)
                },
                { e ->
                    Snackbar.make(news_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                }
            )
        subscriptions.add(subscription)
    }

    private fun initAdapter() {
        if (news_list.adapter == null) {
            news_list.adapter = NewsAdapter()
        }
    }
}