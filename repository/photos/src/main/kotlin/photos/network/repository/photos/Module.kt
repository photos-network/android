/*
 * Copyright 2020-2023 Photos.network developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package photos.network.repository.photos

import androidx.work.WorkManager
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import photos.network.repository.photos.worker.CleanResourcesWorker
import photos.network.repository.photos.worker.SyncLocalPhotosWorker
import photos.network.repository.photos.worker.UploadPhotosWorker

val repositoryPhotosModule = module {
    factory { WorkManager.getInstance(androidApplication()) }

    worker {
        SyncLocalPhotosWorker(
            application = get(),
            workerParameters = get(),
            repository = get(),
        )
    }

    worker {
        CleanResourcesWorker(
            application = get(),
            workerParameters = get(),
        )
    }

    worker {
        UploadPhotosWorker(
            application = get(),
            workerParameters = get(),
            getPhotos = get(),
        )
    }

    single<PhotoRepository> {
        PhotoRepositoryImpl(
            photoApi = get(),
            photoDao = get(),
            workManager = get(),
            mediaStore = get(),
        )
    }
}
