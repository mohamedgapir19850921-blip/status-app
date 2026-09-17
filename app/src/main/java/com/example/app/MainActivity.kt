package com.example.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen()
                }
            }
        }
    }
}

// كل فئة ومعاها قائمة الحالات
val categories: Map<String, List<String>> = mapOf(
    "عامة" to listOf(
        "الدنيا علمتني إني ماشتكيش، وإن شكيت أشكي لربنا بس.",
        "ماتقولش ظروفي صعبة، قول الحمد لله على كل حال.",
        "اللي بيروح لحد ما يجيش، مش خسارة إنك تسيبه.",
        "مش كل اللي بيضحك مبسوط، ومش كل اللي ساكت زعلان.",
        "كون جدع، حتى لو الدنيا مش جدعة معاك.",
        "الصبر مفتاح الفرج، والله ما بينسى حد.",
        "اللي فات مات، واللي جاي في إيد ربنا.",
        "القناعة كنز ما يفنى.",
        "ماتخليش حد يرسملك مستقبلك، إنت اللي ماسك القلم.",
        "خلي عندك مبدأ، وألف صاحب مش يستاهلوك."
    ),
    "دينية" to listOf(
        "سبحان الله وبحمده، سبحان الله العظيم.",
        "الحمد لله على كل حال، وفي كل حال.",
        "لا حول ولا قوة إلا بالله.",
        "اللهم إني أسألك العفو والعافية في الدنيا والآخرة.",
        "توكلت على الله، وهو حسبي ونعم الوكيل.",
        "اللهم اجعلنا من الذاكرين الشاكرين.",
        "الدنيا ساعة، اجعلها طاعة.",
        "من لزم الاستغفار، جعل الله له من كل هم فرجاً.",
        "أستغفر الله العظيم وأتوب إليه.",
        "اللهم إنك عفو تحب العفو فاعف عنا."
    ),
    "رومانسية" to listOf(
        "لو الحب اختيار، كنت اخترتك كل مرة.",
        "إنتي مش تفاصيل يومي، إنتي اليوم بحاله.",
        "قلبي مش محطة، بس إنت الوحيد اللي مر من هنا وعجبه المكان.",
        "كل ما أفكر فيك، بحس إن الدنيا حلوة.",
        "لو الدنيا وردة، إنتي ريحتها.",
        "مش بحب الكلام الكتير، بحب اللي يفهم من نظرة.",
        "إنتي السبب إن ابتسامتي بقت صاحية بدري.",
        "بحبك بطريقة مش محتاجة شرح، محتاجة إحساس بس.",
        "كتير ناس قابلوني، بس إنت الوحيد اللي فضلت.",
        "القلب اللي بيحبش، مش بيبقى قلب."
    ),
    "ضحك" to listOf(
        "أنا مش كسول، أنا في وضع توفير الطاقة.",
        "الفلوس مش بتشتري السعادة، بس بتشتري حاجات أحلى من السعادة.",
        "النوم مش عيب، العيب إنك تصحى بدري.",
        "أنا رياضيّ بطبعه، بريّح على طول.",
        "لو الدنيا ورق، أنا ضهر الصفحة.",
        "بفكر أعمل رجيم، بس التفكير بيجيبلي جوع.",
        "أنا مش عصبي، أنا بس في بيئة غير مناسبة.",
        "المثل بيقول: اللي يستنى حاجة، بتجيله حاجة تانية.",
        "مش فاضي للمشاكل، عندي مصاريف أدور عليها.",
        "ولادي سابوا مية الزمزم، شربوا البيبسي."
    ),
    "شغل" to listOf(
        "اللي بيتعب صح، بيرتاح صح.",
        "النجاح مش بالحظ، بالصبر والاجتهاد.",
        "ماتقولش مش هقدر، جرب الأول.",
        "الشغل عبادة لما تكون نيتك حلال.",
        "التعب النهاردة، راحة بكرة.",
        "الإتقان مش رفاهية، هو الأساس.",
        "اللي بيجري برجليه، بيوصل أسرع من اللي بيسأل الطريق.",
        "الطموح مش عيب، العيب الرضا بالقليل وأنت تقدر على أكتر.",
        "كل بداية صعبة، وكل صعب يهون.",
        "خطط، اشتغل، استمر، وربنا يكرم."
    )
)

@Composable
fun HomeScreen() {
    val context = LocalContext.current

    val categoryNames = categories.keys.toList()
    var selectedCategory by remember { mutableIntStateOf(0) }
    var statusIndex by remember { mutableIntStateOf(0) }

    val currentList = categories[categoryNames[selectedCategory]] ?: emptyList()
    val currentStatus = currentList[statusIndex.coerceIn(0, currentList.lastIndex)]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
                )
            )
            .padding(20.dp)
    ) {
        Text(
            text = "حالات وكلام",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        )

        // تابات الفئات
        ScrollableTabRow(
            selectedTabIndex = selectedCategory,
            containerColor = Color.Transparent,
            edgePadding = 0.dp,
            divider = {}
        ) {
            categoryNames.forEachIndexed { i, name ->
                Tab(
                    selected = i == selectedCategory,
                    onClick = {
                        selectedCategory = i
                        statusIndex = 0
                    },
                    text = {
                        Text(
                            text = name,
                            color = if (i == selectedCategory) Color.White
                                    else Color.White.copy(alpha = 0.6f),
                            fontWeight = if (i == selectedCategory) FontWeight.Bold
                                         else FontWeight.Normal
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // كارت الحالة
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentStatus,
                    color = Color.White,
                    fontSize = 22.sp,
                    lineHeight = 34.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // صف أزرار: حالة جديدة + نسخ
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { statusIndex = (statusIndex + 1) % currentList.size },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("حالة جديدة")
            }

            OutlinedButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("status", currentStatus))
                    Toast.makeText(context, "اتنسخت ✅", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f))
            ) {
                Text("نسخ")
            }
        }

        Spacer(Modifier.height(10.dp))

        // زر المشاركة
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, currentStatus)
                }
                context.startActivity(Intent.createChooser(intent, "شارك الحالة"))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("شارك الحالة 🌟", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
