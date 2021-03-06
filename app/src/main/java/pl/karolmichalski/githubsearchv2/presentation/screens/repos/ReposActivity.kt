package pl.karolmichalski.githubsearchv2.presentation.screens.repos

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import pl.karolmichalski.githubsearchv2.R
import pl.karolmichalski.githubsearchv2.data.models.User
import pl.karolmichalski.githubsearchv2.data.models.base.Identified
import pl.karolmichalski.githubsearchv2.databinding.ActivityReposBinding
import pl.karolmichalski.githubsearchv2.presentation.screens.details.DetailsActivity
import pl.karolmichalski.githubsearchv2.presentation.utils.BundleDelegate
import pl.karolmichalski.githubsearchv2.presentation.utils.hideSoftKeyboard

private const val FIND_REPOS_DELAY = 500L

class ReposActivity : AppCompatActivity(), ReposListener {

	private var Bundle.keywords by BundleDelegate.String("keywords")
	private var Bundle.repoList by BundleDelegate.List<Identified>("reposAndUsers")

	private val handler = Handler(Looper.getMainLooper())
	private var findReposRunnable = Runnable { viewModel.findReposAndUsers() }

	private val viewModel by lazy {
		ViewModelProviders.of(this, ReposViewModel.Factory(application)).get(ReposViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		DataBindingUtil.setContentView<ActivityReposBinding>(this, R.layout.activity_repos).let {
			it.setLifecycleOwner(this)
			it.viewModel = viewModel
			it.listener = this
		}
		viewModel.errorMessage.observe(this, Observer {
			Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
		})
	}

	override fun onSaveInstanceState(outState: Bundle?) {
		super.onSaveInstanceState(outState)
		viewModel.keywords.value?.let { keywords ->
			outState?.keywords = keywords
		}
		viewModel.reposAndUsers.value?.let { repoList ->
			outState?.repoList = repoList
		}
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
		super.onRestoreInstanceState(savedInstanceState)
		viewModel.keywords.value = savedInstanceState?.keywords
		viewModel.reposAndUsers.value = savedInstanceState?.repoList
	}

	override fun onTextChange(charSequence: CharSequence, start: Int, before: Int, count: Int) {
		handler.removeCallbacks(findReposRunnable)
		handler.postDelayed(findReposRunnable, FIND_REPOS_DELAY)
	}

	override fun onUserClick(): (User) -> Unit {
		return { user ->
			val intent = DetailsActivity.getIntent(this, user)
			startActivity(intent)
		}
	}

	override fun onScroll(): () -> Unit {
		return { hideSoftKeyboard() }
	}

}
