package com.example.realestatemanager.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.realestatemanager.R
import com.example.realestatemanager.view.fragment.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var toolbar : Toolbar
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentListEstate = FragmentListEstate()

        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentListEstate).commit()

        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)

        setSupportActionBar(toolbar)
        configureDrawerLayout()
        configureNavigationView()


    }

    private fun configureDrawerLayout(){
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            0,
            0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun configureNavigationView(){
        val navigationView = findViewById<NavigationView>(R.id.activity_main_nav_view)
        navigationView.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val createEstateFragment = CreateEstateFragment()
        val fragmentSearchEstate = FragmentSearchEstate()
        val fragmentListEstate = FragmentListEstate()
        val fragmentMap = MapFragment()
        val fragmentLoan = LoanSimulatorFragment()


        when(item.itemId){
            R.id.create_estate -> {
                Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction().replace(
                    R.id.main_fragment,
                    createEstateFragment
                ).commit()

            }
            R.id.search_estate -> {
                Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction().replace(
                    R.id.main_fragment,
                    fragmentSearchEstate
                ).commit()
            }
            R.id.list_estate -> {
                Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction().replace(
                    R.id.main_fragment,
                    fragmentListEstate
                ).commit()
            }
            R.id.map_menu_drawer -> {
                Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction().replace(
                    R.id.main_fragment,
                    fragmentMap
                ).commit()
            }
            R.id.simulateur_pret -> {
                Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction().replace(
                    R.id.main_fragment,
                    fragmentLoan
                ).commit()
            }
            R.id.settings -> {
                Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}
