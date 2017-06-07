# Griper-android
 A Neighborhood based social network community, that lets you raise voice to public issues and connect better with your neighborhood.



##### **Activities & Fragments**

```
├─ SplashScreenActivity
├─ FacebookLoginActivity
├─ LocationRequestActivity
├─ EmailProfileActivity
    └─ EmailLoginFragment
    └─ EmailSignUpFragment
├─ HomeScreenActivity
    └─ GripesNearbyScreenFragment
    └─ GripesMapScreenFragment
    └─ ProfileScreenFragment
├─ ShowMyLikesActivity
├─ ShowMyPostsActivity
├─ CommentsActivity
├─ ShowGripeDetailsActivity
├─ Camera
        └─ Camera1Activity
        └─ Camera2Activity
├─ PreviewActivity

```
---

#### **Libary to use**
```
Network:
* RxJava
* Retrofit
* OkHttp

Data Parsing:
* Gson

Database
* ActiveAndroid

Image Caching:
* Picasso

Dependancy Injection:
* Dagger 2.0

Logs:
*Timber

Others:
* Butter Knife 7.0.1
* Firebase
* Facebook
* and few others

```

##### **Database Schema**

```
user_profile_data {
  uId,
  name,
  email,
  gender,
  age,
  imageUrl
}

user_preferences_data {
  is_user_logged_in,
  gripe_large_image_width,
  gripe_large_image_height,
  gripe_feed_image_width,
  gripe_feed_image_height,
  gripe_small_image_width,
  gripe_small_image_height,
  last_known_latitude,
  last_known_longitude,
  last_known_address,
  post_code
}

```
#### **SDK Supported**
```
MinSDK : 16
TargetSDK : 25

```

Made with :green_heart: from Sarthak Arora
 
