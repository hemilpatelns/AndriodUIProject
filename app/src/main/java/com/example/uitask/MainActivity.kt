package com.example.uitask

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator

class MainActivity : AppCompatActivity() {
    private lateinit var toolBar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var mainContainer: LinearLayout
    private lateinit var scrollView: ScrollView
    private val sublistMap = mutableMapOf<Int, List<String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)

        mainContainer = findViewById(R.id.mainContainer)
        scrollView = findViewById(R.id.listOptions)

        sublistMap[R.id.textView1] = listOf("Products", "Engagement", "Agency Connect")
        sublistMap[R.id.textView2] = listOf("Claims", "Endorsements")
        sublistMap[R.id.textView3] = listOf("Contact RM", "FAQs", "Raise a concern")

        // closing the drawer
        findViewById<ShapeableImageView>(R.id.closeDrawer).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        findViewById<TextView>(R.id.textView1).setOnClickListener {
            toggleSublistVisibility(
                it as TextView,
                findViewById(R.id.subListContainer1),
                R.drawable.ic_knowledge_centre
            )
        }

        findViewById<TextView>(R.id.textView2).setOnClickListener {
            toggleSublistVisibility(
                it as TextView,
                findViewById(R.id.subListContainer2),
                R.drawable.ic_knowledge_centre
            )
        }

        findViewById<TextView>(R.id.textView3).setOnClickListener {
            toggleSublistVisibility(
                it as TextView,
                findViewById(R.id.subListContainer3),
                R.drawable.ic_help
            )
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolBar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        navigationView = findViewById(R.id.navigationView)

        changeValues()
    }


    private fun toggleSublistVisibility(
        clickedTextView: TextView,
        sublistContainer: LinearLayout,
        drawable: Int
    ) {
        // Check if sublist items are already populated
        val sublistItems = sublistMap[clickedTextView.id]

        clickedTextView.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this@MainActivity,
                drawable,
            ), null, ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.ic_up,
            ), null
        )

        // Toggle visibility of sublistContainer
        if (!sublistItems.isNullOrEmpty()) {
            if (sublistContainer.visibility == View.VISIBLE) {
                sublistContainer.animate()
                    .translationY(-sublistContainer.height.toFloat())
                    .alpha(0f)
                    .setDuration(300)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction {
                        sublistContainer.visibility = View.GONE
                    }
                    .start()
//                sublistContainer.visibility = View.GONE
                clickedTextView.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        drawable,
                    ), null, ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_down,
                    ), null
                )
            } else {
                // Clear existing sublist items in all containers
                clearAllSublistContainers(sublistContainer)

                // Add sublist TextViews to the specified sublistContainer
                for (item in sublistItems) {
                    val textView = TextView(this)
                    textView.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    textView.textSize = 15f
                    textView.text = item
                    textView.setPadding(80, 40, 16, 40)
                    textView.setOnClickListener {
                        // Handle click on sublist item if needed
                    }
                    sublistContainer.addView(textView)
                }

//                sublistContainer.visibility = View.VISIBLE

                sublistContainer.visibility = View.VISIBLE
                sublistContainer.alpha = 0f
                sublistContainer.translationY = -sublistContainer.height.toFloat()
                sublistContainer.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(300)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()

                // Scroll to make the sublist visible
//                val offset = resources.getDimensionPixelOffset(R.dimen.scroll_offset)
                scrollView.post {
                    if(sublistContainer.isVisible){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            scrollView.scrollToDescendant(sublistContainer)
                        }
                    }
                }
            }
        }
    }

    private fun clearAllSublistContainers(sublistContainer: LinearLayout) {
        // Clear sublist containers
        sublistContainer.removeAllViews()
        // Add more sublist containers as needed
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    private fun changeValues() {

        findViewById<CardView>(R.id.quickCardTwo).apply {
            findViewById<TextView>(R.id.numbers).text = "02"
            findViewById<TextView>(R.id.actions).text = "Claims"
            findViewById<TextView>(R.id.days).visibility = View.INVISIBLE
        }
        findViewById<CardView>(R.id.quickCardThree).apply {
            findViewById<TextView>(R.id.numbers).text = "08"
            findViewById<TextView>(R.id.actions).text = "Payments"
            findViewById<TextView>(R.id.days).visibility = View.INVISIBLE
        }

        findViewById<TextView>(R.id.quickActionLabel).apply {
            text = "QUICK ACTIONS"
            setCompoundDrawables(null, null, null, null)
        }

        findViewById<TextView>(R.id.businessSummaryLabel).apply {
            text = "BUSINESS SUMMARY"
            setCompoundDrawables(null, null, null, null)
        }

        findViewById<TextView>(R.id.quickQuotesLabel).apply {
            text = "QUICK QUOTES"
            setCompoundDrawables(null, null, null, null)
        }

        findViewById<TextView>(R.id.giantStepsLabel).apply {
            text = "GIANT STEPS"
        }

        findViewById<TextView>(R.id.campaignLabel).apply {
            text = "CAMPAIGN"
            setCompoundDrawables(null, null, null, null)
        }

        findViewById<TextView>(R.id.healthLabel).text = "Health"
        findViewById<TextView>(R.id.motorLabel).text = "Motor"
        findViewById<TextView>(R.id.travelLabel).text = "Travel"
        findViewById<TextView>(R.id.commLinesLabel).text = "Comm. Lines"

        findViewById<ConstraintLayout>(R.id.healthIcon).apply {
            findViewById<ImageView>(R.id.quickQuotesIcons).setImageResource(R.drawable.ic_health)
            findViewById<TextView>(R.id.quickQuotesItem).text = "Health"
        }

        findViewById<ConstraintLayout>(R.id.motorIcon).apply {
            findViewById<ImageView>(R.id.quickQuotesIcons).setImageResource(R.drawable.ic_motor)
            findViewById<TextView>(R.id.quickQuotesItem).text = "Motor"
        }
        findViewById<ConstraintLayout>(R.id.travelIcon).apply {
            findViewById<ImageView>(R.id.quickQuotesIcons).setImageResource(R.drawable.ic_travel)
            findViewById<TextView>(R.id.quickQuotesItem).text = "Travel"
        }
        findViewById<ConstraintLayout>(R.id.commLinesIcon).apply {
            findViewById<ImageView>(R.id.quickQuotesIcons).setImageResource(R.drawable.ic_comm_lines)
            findViewById<TextView>(R.id.quickQuotesItem).text = "Comm. Lines"
        }

        //1
        findViewById<CardView>(R.id.giantStepsProgress).apply {
            findViewById<TextView>(R.id.yearlyDuration).text = "FY 24-25"
            findViewById<TextView>(R.id.currentDate).text = "As on 12 Jun'24"
            findViewById<TextView>(R.id.campaignName).visibility = View.GONE
            findViewById<ConstraintLayout>(R.id.targetLayout).apply {
                findViewById<TextView>(R.id.targetLabel).text = "Target Premium"
                findViewById<TextView>(R.id.targetValue).text = "₹1.1Cr"
            }
            findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator).apply {
                max = 100
                progress = 25
            }
            findViewById<ImageView>(R.id.imageInsideProgressBar).apply {
                visibility = View.GONE
            }
            findViewById<ConstraintLayout>(R.id.earnedLayout).apply {
                findViewById<TextView>(R.id.earnedLabel).text = "Earned Premium"
                findViewById<TextView>(R.id.earnedValue).text = "₹12.5L"
            }
            findViewById<ConstraintLayout>(R.id.clubGold).visibility = View.GONE
            findViewById<TextView>(R.id.eligibility).apply {
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_not_eligible
                    ), null, null, null
                )
                text = "Not Eligible"
            }
            findViewById<ConstraintLayout>(R.id.linearProgressBarItem).apply {
                findViewById<TextView>(R.id.linearProgressTextOne).text = "Earned"
                findViewById<TextView>(R.id.linearProgressTextTwo).text = "WTD. GWP"
                findViewById<TextView>(R.id.linearProgressTextThree).text = "Target"
                findViewById<LinearProgressIndicator>(R.id.linearProgressBar).apply {
                    max = 100
                    progress = 25
                }
                findViewById<TextView>(R.id.linearEarned).text = "50,000"
                findViewById<TextView>(R.id.linearTarget).text = "2Cr"
                findViewById<TextView>(R.id.goalDifference).apply {
                    text
                    setCompoundDrawables(
                        null,
                        null,
                        null,
                        null
                    )
                }
                findViewById<TextView>(R.id.upcomingSlabTarget).visibility = View.GONE
                findViewById<Button>(R.id.viewProgressButton).text = "View Incentive Details"
            }
        }

        //2
        findViewById<CardView>(R.id.campaignHealth).apply {
            findViewById<TextView>(R.id.yearlyDuration).text = "Quarterly\n02 May - 02 Aug’24"
            findViewById<TextView>(R.id.currentDate).text = "As on 12 Jun'24"
            findViewById<TextView>(R.id.campaignName).text = " Health Quarterly\nCampaign"
            findViewById<ConstraintLayout>(R.id.targetLayout).apply {
                findViewById<TextView>(R.id.targetLabel).text = "Slab Target\n(wtd. GWP)"
                findViewById<TextView>(R.id.targetValue).text = "75K"
            }
            findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator).apply {
                max = 100
                progress = 25
            }
            findViewById<ImageView>(R.id.imageInsideProgressBar).setImageResource(R.drawable.ic_health_blue)
            findViewById<ConstraintLayout>(R.id.earnedLayout).apply {
                findViewById<TextView>(R.id.earnedLabel).text = "Achieved\n(wtd. GWP)"
                findViewById<TextView>(R.id.earnedValue).text = "20.5K"
            }
            findViewById<ConstraintLayout>(R.id.clubGold).visibility = View.GONE
            findViewById<TextView>(R.id.eligibility).apply {
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_eligible
                    ), null, null, null
                )
                text = "Eligible"
            }
            findViewById<ConstraintLayout>(R.id.linearProgressBarItem).apply {
                findViewById<TextView>(R.id.linearProgressTextOne).visibility = View.GONE
                findViewById<TextView>(R.id.linearProgressTextTwo).visibility = View.GONE
                findViewById<TextView>(R.id.linearProgressTextThree).visibility = View.GONE
                findViewById<LinearProgressIndicator>(R.id.linearProgressBar).visibility = View.GONE
                findViewById<TextView>(R.id.linearEarned).visibility = View.GONE
                findViewById<TextView>(R.id.linearTarget).visibility = View.GONE
                findViewById<TextView>(R.id.goalDifference).apply {
                    text = "Upcoming slab target (wtd. GWP)"
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_info),
                        null
                    )
                }
                findViewById<TextView>(R.id.upcomingSlabTarget).text = "1.5L"
                findViewById<Button>(R.id.viewProgressButton).text = "View Campaign"
            }
        }

        //3
        findViewById<CardView>(R.id.campaignMotor).apply {
            findViewById<TextView>(R.id.yearlyDuration).text = "Quarterly\n02 May - 02 Aug’24"
            findViewById<TextView>(R.id.currentDate).text = "As on 12 Jun'24"
            findViewById<TextView>(R.id.campaignName).text = " Motor Quarterly\nCampaign"
            findViewById<ConstraintLayout>(R.id.targetLayout).apply {
                findViewById<TextView>(R.id.targetLabel).text = "Slab Target\n(wtd. GWP)"
                findViewById<TextView>(R.id.targetValue).text = "75K"
            }
            findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator).apply {
                max = 100
                progress = 25
            }
            findViewById<ImageView>(R.id.imageInsideProgressBar).setImageResource(R.drawable.ic_motor_blue)
            findViewById<ConstraintLayout>(R.id.earnedLayout).apply {
                findViewById<TextView>(R.id.earnedLabel).text = "Achieved\n(wtd. GWP)"
                findViewById<TextView>(R.id.earnedValue).text = "20.5K"
            }
            findViewById<ConstraintLayout>(R.id.clubGold).visibility = View.GONE
            findViewById<TextView>(R.id.eligibility).apply {
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_not_eligible
                    ), null, null, null
                )
                text = "Not Eligible"
            }
            findViewById<ConstraintLayout>(R.id.linearProgressBarItem).apply {
                findViewById<TextView>(R.id.linearProgressTextOne).visibility = View.GONE
                findViewById<TextView>(R.id.linearProgressTextTwo).visibility = View.GONE
                findViewById<TextView>(R.id.linearProgressTextThree).visibility = View.GONE
                findViewById<LinearProgressIndicator>(R.id.linearProgressBar).visibility = View.GONE
                findViewById<TextView>(R.id.linearEarned).visibility = View.GONE
                findViewById<TextView>(R.id.linearTarget).visibility = View.GONE
                findViewById<TextView>(R.id.goalDifference).apply {
                    text = "Upcoming slab target (wtd. GWP)"
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_info),
                        null
                    )
                }
                findViewById<TextView>(R.id.upcomingSlabTarget).text = "1.5L"
                findViewById<Button>(R.id.viewProgressButton).text = "View Campaign"
            }
        }

        //4
        findViewById<CardView>(R.id.campaignTravel).apply {
            findViewById<TextView>(R.id.yearlyDuration).text = "Quarterly\n02 May - 02 Aug’24"
            findViewById<TextView>(R.id.currentDate).text = "As on 12 Jun'24"
            findViewById<TextView>(R.id.campaignName).text = " Travel 24\nCampaign"
            findViewById<ConstraintLayout>(R.id.targetLayout).apply {
                findViewById<TextView>(R.id.targetLabel).text = "Slab Target\n(wtd. GWP)"
                findViewById<TextView>(R.id.targetValue).text = "75K"
            }
            findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator).apply {
                max = 100
                progress = 25
            }
            findViewById<ImageView>(R.id.imageInsideProgressBar).setImageResource(R.drawable.ic_travel_blue)
            findViewById<ConstraintLayout>(R.id.earnedLayout).apply {
                findViewById<TextView>(R.id.earnedLabel).text = "Achieved\n(wtd. GWP)"
                findViewById<TextView>(R.id.earnedValue).text = "20.5K"
            }
            findViewById<ConstraintLayout>(R.id.clubGold).visibility = View.GONE
            findViewById<TextView>(R.id.eligibility).apply {
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_eligible
                    ), null, null, null
                )
                text = "Eligible"
            }
            findViewById<ConstraintLayout>(R.id.linearProgressBarItem).apply {
                findViewById<TextView>(R.id.linearProgressTextOne).visibility = View.GONE
                findViewById<TextView>(R.id.linearProgressTextTwo).visibility = View.GONE
                findViewById<TextView>(R.id.linearProgressTextThree).visibility = View.GONE
                findViewById<LinearProgressIndicator>(R.id.linearProgressBar).visibility = View.GONE
                findViewById<TextView>(R.id.linearEarned).visibility = View.GONE
                findViewById<TextView>(R.id.linearTarget).visibility = View.GONE
                findViewById<TextView>(R.id.goalDifference).apply {
                    text = "Upcoming slab target (wtd. GWP)"
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_info),
                        null
                    )
                }
                findViewById<TextView>(R.id.upcomingSlabTarget).text = "1.5L"
                findViewById<Button>(R.id.viewProgressButton).text = "View Campaign"
            }
        }

        //5
        findViewById<CardView>(R.id.campaignCommLines).apply {
            findViewById<TextView>(R.id.yearlyDuration).text = "Quarterly\n02 May - 02 Aug’24"
            findViewById<TextView>(R.id.currentDate).text = "As on 12 Jun'24"
            findViewById<TextView>(R.id.campaignName).text = "Comm. Lines Quarterly\nCampaign"
            findViewById<ConstraintLayout>(R.id.targetLayout).apply {
                findViewById<TextView>(R.id.targetLabel).text = "Slab Target"
                findViewById<TextView>(R.id.targetValue).text = "75K"
            }
            findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator).apply {
                max = 75000
                progress = 55000
            }
            findViewById<ImageView>(R.id.imageInsideProgressBar).setImageResource(R.drawable.ic_comm_lines_blue)
            findViewById<ConstraintLayout>(R.id.earnedLayout).apply {
                findViewById<TextView>(R.id.earnedLabel).text = "Achieved"
                findViewById<TextView>(R.id.earnedValue).text = "55K"
            }
//            findViewById<ConstraintLayout>(R.id.clubGold).visibility = View.GONE
            findViewById<TextView>(R.id.eligibility).apply {
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_eligible
                    ), null, null, null
                )
                text = "Eligible"
            }
            findViewById<ConstraintLayout>(R.id.linearProgressBarItem).apply {
                findViewById<TextView>(R.id.linearProgressTextOne).visibility = View.GONE
                findViewById<TextView>(R.id.linearProgressTextTwo).visibility = View.GONE
                findViewById<TextView>(R.id.linearProgressTextThree).visibility = View.GONE
                findViewById<LinearProgressIndicator>(R.id.linearProgressBar).visibility = View.GONE
                findViewById<TextView>(R.id.linearEarned).visibility = View.GONE
                findViewById<TextView>(R.id.linearTarget).visibility = View.GONE
                findViewById<TextView>(R.id.goalDifference).apply {
                    text = "Upcoming slab target"
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_info),
                        null
                    )
                }
                findViewById<TextView>(R.id.upcomingSlabTarget).text = "₹25L"
                findViewById<Button>(R.id.viewProgressButton).text = "View Campaign"
            }
        }
    }
}