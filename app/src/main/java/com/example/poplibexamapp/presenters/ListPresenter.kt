package com.example.poplibexamapp.presenters

import com.example.poplibexamapp.*
import com.example.poplibexamapp.data.MovieDataClass
import com.example.poplibexamapp.data.MoviesList
import com.example.poplibexamapp.database.LocalStorage
import com.example.poplibexamapp.netSource.ApiHolder
import com.example.poplibexamapp.presentations.DetailsScreen
import com.example.poplibexamapp.presentations.ListFragmentView
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import moxy.MvpPresenter

class ListPresenter(
    //private val customSchedulers: CustomSchedulersInterface,
    private val router: Router,
    //private val repository: MainRepositoryInterface,
    private val dataBase: LocalStorage,
    private val networkStatus: NetworkStatus
): MvpPresenter<ListFragmentView>() {

    class ListItemsPresenter : IListViewPresenter {
        val mainList = mutableListOf<MovieDataClass>()
        override var itemClickListener: ((ListItemView) -> Unit)? = null

        override fun getCount() = mainList.size

        override fun bindView(view: ListItemView) {
            val listItem = mainList[view.pos]
            view.setTitle(listItem.title)
            view.setPoster("http://image.tmdb.org/t/p/w500${listItem.poster_path}")
        }
    }

    val listItemsPresenter = ListItemsPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initRVList()
        loadData()

        listItemsPresenter.itemClickListener = { itemView ->
            val item = listItemsPresenter.mainList[itemView.pos]
            router.navigateTo(DetailsScreen(item.id.toString()))
        }
    }

    private val disposable = CompositeDisposable()

    private val repository = MainRepository(ApiHolder.api, dataBase, networkStatus)

    private fun loadData() {
        disposable.add(
            repository.getMoviesList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {onResult(it)},
                    {println(it)}
                )
        )
    }

    private fun onResult(list: MoviesList) {
        listItemsPresenter.mainList.clear()
        listItemsPresenter.mainList.addAll(list.results)
        viewState.setList()
    }

    fun backButtonClicked(){
        router.finishChain()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}