package com.bangkit.gocomplaint

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.gocomplaint.data.repository.ComplaintRepository
import com.bangkit.gocomplaint.data.repository.NeedHeaderRepository
import com.bangkit.gocomplaint.data.repository.UserRepository
import com.bangkit.gocomplaint.di.Injection
import com.bangkit.gocomplaint.ui.screen.add.AddViewModel
import com.bangkit.gocomplaint.ui.screen.detail.DetailViewModel
import com.bangkit.gocomplaint.ui.screen.profile.ProfileViewModel
import com.bangkit.gocomplaint.ui.screen.home.HomeViewModel
import com.bangkit.gocomplaint.ui.screen.login.LoginViewModel
import com.bangkit.gocomplaint.ui.screen.register.RegisterViewModel
import com.bangkit.gocomplaint.ui.screen.search.SearchViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val complaintRepository: ComplaintRepository,
    private val needHeaderRepository: NeedHeaderRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(userRepository, complaintRepository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository, needHeaderRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(userRepository, complaintRepository, needHeaderRepository) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(userRepository, complaintRepository) as T
        } else if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(userRepository, needHeaderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideUserRepository(context),
                        Injection.provideComplaintRepository(),
                        Injection.provideHeaderRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}