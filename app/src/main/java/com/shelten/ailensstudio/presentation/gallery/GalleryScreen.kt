package com.shelten.ailensstudio.presentation.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.shelten.ailensstudio.data.model.GalleryImage

private val BackgroundColor = Color(0xFFF9FAFD)
private val PrimaryBlue = Color(0xFF1769FF)
private val DarkText = Color(0xFF111827)
private val SecondaryText = Color(0xFF667085)
private val CardWhite = Color.White

@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = viewModel(),
    onImageClick: (GalleryImage) -> Unit = {}
) {
    val images by viewModel.images.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadImages()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            GalleryHeader()

            HeroCard()

            QuickCategories(
                imageCount = images.size
            )

            RecentPhotosHeader()

            when {

                isLoading -> {
                    LoadingState()
                }

                error != null -> {
                    ErrorState(
                        message = error ?: "Something went wrong.",
                        onRetry = viewModel::loadImages
                    )
                }

                images.isEmpty() -> {
                    EmptyState()
                }

                else -> {
                    GalleryGrid(
                        images = images,
                        onImageClick = onImageClick
                    )
                }
            }
        }

        BottomNavigation(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun GalleryHeader() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 22.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "AI Lens Studio",
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = "Your photos, reimagined",
                fontSize = 14.sp,
                color = SecondaryText
            )
        }

        HeaderAction(
            icon = Icons.Default.Search,
            contentDescription = "Search"
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        HeaderAction(
            icon = Icons.Default.MoreVert,
            contentDescription = "More"
        )
    }
}

@Composable
private fun HeaderAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {

    Surface(
        modifier = Modifier.size(48.dp),
        shape = CircleShape,
        color = CardWhite,
        shadowElevation = 2.dp
    ) {

        IconButton(
            onClick = {}
        ) {

            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = DarkText
            )
        }
    }
}

@Composable
private fun HeroCard() {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(26.dp),
        color = Color(0xFFE8F1FF),
        shadowElevation = 1.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(24.dp)
            ) {

                Text(
                    text = "TURN PHOTOS INTO",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.6.sp,
                    color = Color(0xFF61708A)
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Text(
                    text = "Something\nExtraordinary",
                    fontSize = 29.sp,
                    lineHeight = 31.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Text(
                    text = "Edit, enhance and create with AI.",
                    fontSize = 13.sp,
                    color = SecondaryText
                )

                Spacer(
                    modifier = Modifier.height(13.dp)
                )

                Button(
                    onClick = {},
                    shape = RoundedCornerShape(50.dp)
                ) {

                    Text(
                        text = "Try AI Edit"
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickCategories(
    imageCount: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {

        CategoryCard(
            modifier = Modifier.weight(1f),
            title = "All Photos",
            value = imageCount.toString(),
            selected = true
        )

        CategoryCard(
            modifier = Modifier.weight(1f),
            title = "Favorites",
            value = "0"
        )

        CategoryCard(
            modifier = Modifier.weight(1f),
            title = "Screenshots",
            value = "0"
        )

        CategoryCard(
            modifier = Modifier.weight(1f),
            title = "Videos",
            value = "0"
        )
    }
}

@Composable
private fun CategoryCard(
    modifier: Modifier,
    title: String,
    value: String,
    selected: Boolean = false
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = if (selected) {
            Color(0xFFEAF2FF)
        } else {
            CardWhite
        },
        shadowElevation = if (selected) 0.dp else 1.dp
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 13.dp
            )
        ) {

            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (selected) {
                    PrimaryBlue
                } else {
                    DarkText
                }
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
        }
    }
}

@Composable
private fun RecentPhotosHeader() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 1.dp,
                bottom = 5.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Recent Photos",
            modifier = Modifier.weight(1f),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        TextButton(
            onClick = {}
        ) {

            Text(
                text = "See all",
                color = Color(0xFF68704E),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun GalleryGrid(
    images: List<GalleryImage>,
    onImageClick: (GalleryImage) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(
            bottom = 120.dp
        )
    ) {

        items(
            items = images,
            key = { it.id }
        ) { image ->

            AsyncImage(
                model = image.uri,
                contentDescription = image.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        onImageClick(image)
                    }
            )
        }
    }
}

@Composable
private fun BottomNavigation(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 14.dp,
                end = 14.dp,
                bottom = 10.dp
            )
            .navigationBarsPadding()
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp),
            shape = RoundedCornerShape(30.dp),
            color = Color.White,
            shadowElevation = 10.dp
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                NavigationItem(
                    icon = Icons.Default.Home,
                    title = "Gallery",
                    selected = true
                )

                NavigationItem(
                    icon = Icons.Default.FolderOpen,
                    title = "Albums"
                )

                Spacer(
                    modifier = Modifier.width(62.dp)
                )

                NavigationItem(
                    icon = Icons.Default.AutoAwesome,
                    title = "AI Edit"
                )

                NavigationItem(
                    icon = Icons.Default.Person,
                    title = "Profile"
                )
            }
        }

        Surface(
            modifier = Modifier
                .size(62.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-18).dp),
            shape = CircleShape,
            color = PrimaryBlue,
            shadowElevation = 10.dp
        ) {

            IconButton(
                onClick = {}
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
private fun NavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    selected: Boolean = false
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (selected) {
                PrimaryBlue
            } else {
                Color(0xFF667085)
            },
            modifier = Modifier.size(24.dp)
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
            color = if (selected) {
                PrimaryBlue
            } else {
                Color(0xFF667085)
            }
        )
    }
}

@Composable
private fun LoadingState() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            color = PrimaryBlue
        )
    }
}

@Composable
private fun EmptyState() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "No photos available",
            color = SecondaryText
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = message,
            color = SecondaryText
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onRetry
        ) {

            Text("Retry")
        }
    }
}