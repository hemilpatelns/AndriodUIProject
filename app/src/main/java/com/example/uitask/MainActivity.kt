package com.example.uitask

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.core.view.marginEnd
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.uitask.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator


class MainActivity : AppCompatActivity() {

    private lateinit var tooltipFrameLayout: FrameLayout

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var toolBar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var mainContainer: LinearLayout
    private lateinit var scrollView: ScrollView

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductCardAdapter
    private lateinit var dataList: List<MyDataModel>
    private val sublistMap = mutableMapOf<Int, List<String>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)

        mainContainer = findViewById(R.id.mainContainer)
        scrollView = findViewById(R.id.listOptions)

        productCardDetails()
        motorCardDetails()

        sublistMap[R.id.textView1] = listOf("Products", "Engagement", "Agency Connect")
        sublistMap[R.id.textView2] = listOf("Claims", "Endorsements")
        sublistMap[R.id.textView3] = listOf("Contact RM", "FAQs", "Raise a concern")

        // Closing the drawer
        findViewById<ShapeableImageView>(R.id.closeDrawer).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        // Tool Tip on click event
        binding.giantStepsLabel.headerInfo.setOnClickListener {
            showTooltip(it)
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

    //   Function to show tooltip
    private fun showTooltip(anchorView: View) {
        // Inflate the custom tooltip layout
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val tooltipView = inflater.inflate(R.layout.item_tooltip, null)


        val popupWindow = PopupWindow(
            tooltipView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Show the popup window
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        val xOffSet = -80
        val yOffSet = 0
        popupWindow.showAsDropDown(anchorView, xOffSet, yOffSet)
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
                    .setDuration(500)
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
            } else if(sublistContainer.visibility == View.GONE) {
                // Clear existing sublist items in all containers
                clearAllSublistContainers(sublistContainer)

                // Add sublist TextViews to the specified sublistContainer
                for (item in sublistItems) {
                    val textView = TextView(this)
                    textView.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    textView.text = item
                    textView.setPadding(80, 40, 16, 40)
                    textView.setOnClickListener {
                        // Handle click on sublist item if needed
                    }
                    sublistContainer.addView(textView)
                }

//                sublistContainer.visibility = View.VISIBLE
                sublistContainer.alpha = 0f
                sublistContainer.translationY = -sublistContainer.height.toFloat()
                sublistContainer.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(500)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
                sublistContainer.visibility = View.VISIBLE
                // Scroll to make the sublist visible
//                val offset = resources.getDimensionPixelOffset(R.dimen.scroll_offset)
                scrollView.post {
                    if (sublistContainer.isVisible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            scrollView.scrollToDescendant(sublistContainer)
                        }
                    }
                }
            }
        }else{

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

        binding.quickActionLabel.apply {
            headerText.text = "Quick Actions"
            headerInfo.visibility = View.GONE
        }

        binding.businessSummaryLabel.apply {
            headerText.text = "Business Summary"
            headerInfo.visibility = View.GONE
        }

        binding.quickQuotesLabel.apply {
            headerText.text = "Quick Quote"
            headerInfo.visibility = View.GONE
        }

        binding.giantStepsLabel.apply {
            headerText.text = "Giant Steps"
        }

        binding.campaignLabel.apply {
            headerText.text = "Campaign"
            headerInfo.visibility = View.GONE
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
            findViewById<ConstraintLayout>(R.id.clubGold).visibility = View.INVISIBLE
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
            findViewById<ConstraintLayout>(R.id.clubGold).visibility = View.INVISIBLE
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

    private fun productCardDetails() {
        dataList = listOf(
            MyDataModel(
                R.drawable.ic_insurance,
                "Product 1",
                "Copy up to three\nwill come\nhere",
                "Lorem ipsem upto 2\nlines",
                "Know More"
            ),
            MyDataModel(
                R.drawable.ic_insurance,
                "Product 2",
                "Copy up to three\nwill come\nhere",
                "Lorem ipsem upto 2\nlines",
                "Know More"
            ),
            MyDataModel(
                R.drawable.ic_insurance,
                "Product 3",
                "Copy up to three\nwill come\nhere",
                "Lorem ipsem upto 2\nlines",
                "Know More"
            )
            // Add more items as needed
        )

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.insuranceCards)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Initialize adapter
        adapter = ProductCardAdapter(dataList)
        recyclerView.adapter = adapter
    }

    private fun motorCardDetails() {
        val motorData = MotorData(
            yearlyDuration = "Quarterly\n02 May - 02 Aug’24",
            currentDate = "As on 12 Jun'24",
            campaignName = "Motor Quarterly\nCampaign",
            targetLabel = "Slab Target\n(wtd. GWP)",
            targetValue = "75K",
            R.drawable.ic_motor_blue,
            progressMax = 100,
            progressCurrent = 25,
            earnedLabel = "Achieved\n(wtd. GWP)",
            earnedValue = "20.5K",
            eligibilityText = "Not Eligible",
            goalDifference = "Upcoming slab target (wtd. GWP)",
            upcomingTarget = "1.5L",
            viewProgress = "View Motor Campaign"
        )
        val motorList: MutableList<MotorData> = mutableListOf()
        for (i in 0 until 4) {
            motorList.add(motorData)
        }

        // Dot Code
        val dotIndicatorContainer: LinearLayout = findViewById(R.id.dotIndicatorContainer)

        // Initialize number of dots based on the number of cards
        val numCards = motorList.size  // Example: Replace with your actual number of cards

        // Create an array of ImageView (dots)
        val dots = arrayOfNulls<ImageView>(numCards)

        val activeDotDrawable =
            ContextCompat.getDrawable(this, R.drawable.ic_active_dot) // Active dot drawable
        val inactiveDotDrawable =
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_dot) // Inactive dot drawable

        // Add dots to the dotIndicatorContainer
        for (i in 0 until numCards) {
            dots[i] = ImageView(this)
            dots[i]?.setImageDrawable(inactiveDotDrawable)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(15, 0, 15, 0)
            dots[i]?.layoutParams = params
            dotIndicatorContainer.addView(dots[i])
        }

        // Function to update dot indicators based on current position
        fun updateDotIndicator(position: Int) {
            for (i in dots.indices) {
                if (i == position) {
                    dots[i]?.setImageDrawable(activeDotDrawable)
                } else {
                    dots[i]?.setImageDrawable(inactiveDotDrawable)
                }
            }
        }

        val recyclerViewMotor: RecyclerView = binding.recyclerViewMotor

        recyclerViewMotor.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerViewMotor.layoutManager as LinearLayoutManager
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                updateDotIndicator(firstVisiblePosition)
            }
        })

        recyclerViewMotor.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(recyclerViewMotor)
        val motorAdapter = MotorAdapter(motorList)
        recyclerViewMotor.adapter = motorAdapter
    }
}