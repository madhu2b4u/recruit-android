package nz.co.test.transactions.main.presentation.activities

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import nz.co.test.transactions.databinding.ActivityMainBinding
import nz.co.test.transactions.util.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

}